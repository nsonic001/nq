package com.nilesh.nq.producer;

import static org.junit.jupiter.api.Assertions.*;

import com.nilesh.nq.consumer.LLQueueConsumer;
import com.nilesh.nq.queue.LinkedListQueue;
import com.nilesh.nq.service.MetaDataService;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class LLQueueProducerTest {

  @Test
  void testProduce() {
    MetaDataService metaDataService = new MetaDataService();
    LLQueueProducer producer = new LLQueueProducer(metaDataService);
    LinkedListQueue queue = new LinkedListQueue("queue");
    JSONObject json = new JSONObject(); json.put("key", "value");
    producer.produce(queue, json);
    assertEquals(queue.getHead().getItem(), json);
  }

  @Test
  void testCallCallbacks(){
    MetaDataService metaDataService = new MetaDataService();
    LLQueueProducer producer = new LLQueueProducer(metaDataService);
    LinkedListQueue queue = metaDataService.getOrCreateQueue("q1");
    metaDataService.registerConsumer("q1", "c1",
        "http://localhost:8000");
    LLQueueConsumer consumer = metaDataService.getConsumer("c1");
    JSONObject json = new JSONObject(); json.put("key", "value");
    producer.produce(queue, json);
    assertEquals(consumer.consume("q1").getJsonObject(), json);
  }
}