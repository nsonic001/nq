package com.nilesh.nq.consumer;

import com.nilesh.nq.queue.LinkedListQueue;
import com.nilesh.nq.queue.Node;
import com.nilesh.nq.response.ConsumeMsgResponse;
import com.nilesh.nq.service.MetaDataService;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.util.Pair;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@NoArgsConstructor
@AllArgsConstructor
public class LLQueueConsumer implements NqConsumer {
  private String name;
  HashMap<String, Pair<LinkedListQueue, Node>> offsetMap = new HashMap<>();

  MetaDataService metaDataService;

  @Autowired
  public LLQueueConsumer(MetaDataService metaDataService) {
    this.metaDataService = metaDataService;
  }

  @Override
  public ConsumeMsgResponse consume(String queueName) {
    if(offsetMap.containsKey(queueName)){
      LinkedListQueue lQueue = offsetMap.get(queueName).getFirst();
      Node offsetNode = offsetMap.get(queueName).getSecond();
      if(offsetNode.getTimestamp()<(System.currentTimeMillis()-lQueue.getMaxRetention())){
        // if offset node is out of retention reassign to tail
        offsetNode = lQueue.getTail();
      }
      if(offsetNode.getPrevious()!=null){
        Node readNode = offsetNode.getPrevious();
        setOffset(queueName, lQueue, readNode);
        return ConsumeMsgResponse.builder().jsonObject(readNode.getItem())
            .offset(readNode.getOffset()).queueName(lQueue.getName()).build();
      }else{
        // Return same offset with null jsonObject if no further data for queue
        return ConsumeMsgResponse.builder().offset(offsetNode.getOffset())
            .queueName(lQueue.getName()).build();
      }

    }else{
      LinkedListQueue queue = this.metaDataService.getQueue(queueName);
      if(queue==null){
        return ConsumeMsgResponse.builder().build();
      }else if(queue.getTail()!=null){
        setOffset(queueName, queue, queue.getTail());
        return ConsumeMsgResponse.builder().jsonObject(queue.getTail().getItem())
            .offset(queue.getTail().getOffset()).queueName(queue.getName()).build();
      }else {
        // Return with null jsonObject if no further data for queue
        return ConsumeMsgResponse.builder().queueName(queue.getName()).build();
      }
    }
  }

  private Node consumeWithoutCommit(String queueName) {
    return offsetMap.get(queueName).getSecond();
  }

  @Override
  public ConsumeMsgResponse consume(String queueName, long offset) {
    LinkedListQueue queue = this.metaDataService.getQueue(queueName);
    if(queue==null){
      return ConsumeMsgResponse.builder().build();
    }else if(queue.getTail()==null){
      return ConsumeMsgResponse.builder().queueName(queue.getName()).build();
    }
    else if(queue.getTail().getOffset()>offset || queue.getHead().getOffset()<offset){
      return ConsumeMsgResponse.builder().queueName(queue.getName()).build();
    }else {
      Node offsetNode = queue.getTail();

      while (offsetNode!=null && offsetNode.getOffset()<offset){
        offsetNode = offsetNode.getPrevious();
      }
      if(offsetNode!=null){
        setOffset(queueName, queue, offsetNode);
        return ConsumeMsgResponse.builder().jsonObject(offsetNode.getItem())
            .offset(offsetNode.getOffset()).queueName(queue.getName()).build();
      }else{
        // Return with null jsonObject if no further data for queue
        return ConsumeMsgResponse.builder().queueName(queue.getName()).build();
      }

    }
  }

  @Override
  public void offsetReset(String queueName) {
    if(offsetMap.containsKey(queueName)){
      offsetMap.remove(queueName);
    }
  }

  private void setOffset(String queueName, LinkedListQueue queue, Node node) {
    offsetMap.put(queueName, Pair.of(queue, node));
  }

  @Retryable(maxAttempts = 3)
  public void makeCallbackRequest(String queueName, String callbackURL)
      throws IOException {
    URL url = new URL(callbackURL);
    URLConnection con = url.openConnection();
    HttpURLConnection http = (HttpURLConnection)con;
    http.setRequestMethod("POST");
    Node msgNode = this.consumeWithoutCommit(queueName);
    ConsumeMsgResponse response = ConsumeMsgResponse.builder().offset(msgNode.getOffset())
        .queueName(queueName).jsonObject(msgNode.getItem()).build();
    http.setRequestProperty("Content-Type", "application/json");
    try(OutputStream os = con.getOutputStream()) {
      byte[] input = response.toString().getBytes("utf-8");
      os.write(input, 0, input.length);
    }
    http.setConnectTimeout(500);
    http.setDoOutput(true);
    if(msgNode.getPrevious()!=null){
      this.setOffset(queueName, metaDataService.getQueue(queueName), msgNode.getPrevious());
    }else{
      offsetMap.remove(queueName);
    }
  }

}
