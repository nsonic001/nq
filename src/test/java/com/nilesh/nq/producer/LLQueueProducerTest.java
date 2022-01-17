package com.nilesh.nq.producer;

import static org.junit.jupiter.api.Assertions.*;

import com.nilesh.nq.queue.LinkedListQueue;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.Test;

class LLQueueProducerTest {

  @Test
  void testProduce() {
    LLQueueProducer producer = new LLQueueProducer();
    LinkedListQueue queue = new LinkedListQueue("queue");
    JSONObject json = new JSONObject(); json.put("key", "value");
    producer.produce(queue, json);
    assertEquals(queue.getHead().getItem(), json);
  }
}