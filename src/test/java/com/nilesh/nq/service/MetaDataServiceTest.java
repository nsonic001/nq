package com.nilesh.nq.service;

import static org.junit.jupiter.api.Assertions.*;

import com.nilesh.nq.consumer.LLQueueConsumer;
import com.nilesh.nq.queue.LinkedListQueue;
import org.junit.jupiter.api.Test;

class MetaDataServiceTest {

  @Test
  void getOrCreateQueue() {
    MetaDataService metaDataService = new MetaDataService();
    LinkedListQueue queue = metaDataService.getOrCreateQueue("queue1");
    assertEquals(queue.getName(), "queue1");
    assertNull(queue.getHead());
    assertNull(queue.getTail());
    LinkedListQueue queue1 = metaDataService.getOrCreateQueue("queue1");
    assertEquals(queue, queue1);
  }

  @Test
  void getOrCreateConsumer() {
    MetaDataService metaDataService = new MetaDataService();
    LLQueueConsumer consumer = metaDataService.getOrCreateConsumer("consumer1");
    assertEquals(consumer.getName(), "consumer1");
    assertEquals(consumer.getOffsetMap().size(), 0);
    LLQueueConsumer consumer1 = metaDataService.getOrCreateConsumer("consumer1");
    assertEquals(consumer, consumer1);
  }
}