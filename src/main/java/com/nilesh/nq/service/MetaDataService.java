package com.nilesh.nq.service;

import com.nilesh.nq.consumer.LLQueueConsumer;
import com.nilesh.nq.queue.LinkedListQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Service
@Getter
@Setter
@NoArgsConstructor
public class MetaDataService {

  private ConcurrentMap<String, LinkedListQueue> queueMap = new ConcurrentHashMap<>();
  private ConcurrentMap<String, LLQueueConsumer> consumerMap = new ConcurrentHashMap<>();


  public LinkedListQueue getQueue(String name){
    return queueMap.getOrDefault(name, null);
  }

  public LinkedListQueue getOrCreateQueue(String name){
    LinkedListQueue queue = this.getQueue(name);
    if(queue==null){
      queue = new LinkedListQueue(name);
      queueMap.put(name, queue);
    }
    return queue;
  }

  public LLQueueConsumer getConsumer(String name){
    return consumerMap.getOrDefault(name, null);
  }

  public LLQueueConsumer getOrCreateConsumer(String name){
    LLQueueConsumer consumer = this.getConsumer(name);
    if(consumer==null){
      consumer = new LLQueueConsumer(this);
      consumer.setName(name);
      consumerMap.put(name, consumer);
    }
    return consumer;
  }

  public void queueCleanup(){
    for(String key: queueMap.keySet()){
      System.out.println(String.format("Queue cleanup for {}", key));
      queueMap.get(key).dequeueOld(System.currentTimeMillis());
    }
    System.out.println("Queue cleanup is done");
  }
}
