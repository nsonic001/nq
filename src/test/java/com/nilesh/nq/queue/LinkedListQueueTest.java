package com.nilesh.nq.queue;

import static org.junit.jupiter.api.Assertions.*;

import org.json.simple.JSONObject;
import org.junit.jupiter.api.Test;

class LinkedListQueueTest {

  @Test
  void testEnqueue() {
    JSONObject json = new JSONObject(); json.put("key", "value");
    LinkedListQueue queue = new LinkedListQueue("queue");
    queue.enqueue(json);
    assertEquals(queue.getHead().getItem(), json);
    assertEquals(queue.getTail().getItem(), json);
    assertEquals(queue.getLastOffset(), 1);
    assertEquals(queue.getHead().getOffset(), 1);
    JSONObject json1 = new JSONObject(); json1.put("key", "value1");
    queue.enqueue(json1);
    assertEquals(queue.getHead().getItem(), json1);
    assertEquals(queue.getTail().getItem(), json);
    assertEquals(queue.getLastOffset(), 2);
    assertEquals(queue.getHead().getOffset(), 2);
  }

  @Test
  void testDequeueOld() {
    JSONObject json = new JSONObject();
    json.put("key", "value");
    LinkedListQueue queue = new LinkedListQueue("queue");
    queue.enqueue(json);
    queue.getHead().setTimestamp(100);
    queue.enqueue(json);
    queue.getHead().setTimestamp(100);
    queue.enqueue(json);
    queue.dequeueOld(1000);
    assertNull(queue.getHead().getNext());
    assertEquals(queue.getTail(), queue.getHead());
  }

  @Test
  void testReadFromOffset() {
    LinkedListQueue queue = new LinkedListQueue("queue");
    JSONObject json = new JSONObject(); json.put("key", "value");
    queue.enqueue(json);
    JSONObject json1 = new JSONObject(); json1.put("key", "value");
    queue.enqueue(json1);
    JSONObject json2 = new JSONObject(); json2.put("key", "value");
    queue.enqueue(json2);
    Node node = queue.readFromOffset(2);
    assertEquals(node.getOffset(), 2);
    assertEquals(node.getItem(), json1);
  }

}