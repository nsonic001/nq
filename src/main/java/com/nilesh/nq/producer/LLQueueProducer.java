package com.nilesh.nq.producer;

import com.nilesh.nq.queue.LinkedListQueue;
import com.nilesh.nq.service.MetaDataService;
import java.util.ArrayList;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class LLQueueProducer implements NqProducer{

  MetaDataService metaDataService;

  @Autowired
  public LLQueueProducer(MetaDataService metaDataService){
    this.metaDataService = metaDataService;
  }

  @Override
  public void produce(LinkedListQueue queue, JSONObject jsonObject) {
    queue.enqueue(jsonObject);
    this.callCallbacks(queue);
  }

  @Async
  public void callCallbacks(LinkedListQueue queue){

      ArrayList<Pair<String, String>> consumers = this.metaDataService
          .getCallbackConsumersForQueue(queue.getName());
      if(consumers!=null){
        consumers.parallelStream().forEach((callbackMap) -> {
          try {
            this.metaDataService.getConsumer(callbackMap.getFirst()).makeCallbackRequest(
                queue.getName(), callbackMap.getSecond());
          } catch (Exception e) {
            System.out.println("error while making callback requests: " + e.toString());
          }
        });
      }
  }

}
