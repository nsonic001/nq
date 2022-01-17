package com.nilesh.nq.consumer;

import static org.junit.jupiter.api.Assertions.*;

import com.nilesh.nq.producer.LLQueueProducer;
import com.nilesh.nq.queue.LinkedListQueue;
import com.nilesh.nq.response.ConsumeMsgResponse;
import com.nilesh.nq.service.MetaDataService;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LLQueueConsumerTest {

  @Test
  void testConsumeWithQueueDoesNotExists() {
    MetaDataService metaDataService = new MetaDataService();
    LLQueueConsumer consumer = new LLQueueConsumer(metaDataService);
    ConsumeMsgResponse response = consumer.consume("queue");
    assertNull(response.getQueueName());
    assertNull(response.getJsonObject());
    assertEquals(response.getOffset(), 0);
  }

  @Test
  void testConsumeWithQueueExistsNoData() {
    MetaDataService metaDataService = new MetaDataService();
    LLQueueConsumer consumer = new LLQueueConsumer(metaDataService);
    metaDataService.getOrCreateQueue("queue");
    ConsumeMsgResponse response = consumer.consume("queue");
    assertEquals(response.getQueueName(), "queue");
    assertNull(response.getJsonObject());
    assertEquals(response.getOffset(), 0);
  }

  @Test
  void testConsumeWithQueueAndDataExists() {
    MetaDataService metaDataService = new MetaDataService();
    LLQueueConsumer consumer = new LLQueueConsumer(metaDataService);
    LinkedListQueue queue = metaDataService.getOrCreateQueue("queue");
    LLQueueProducer producer = new LLQueueProducer();
    JSONObject json = new JSONObject(); json.put("key", "value");
    JSONObject json1 = new JSONObject(); json1.put("key", "value1");
    JSONObject json2 = new JSONObject(); json2.put("key", "value2");
    JSONObject json3 = new JSONObject(); json3.put("key", "value3");
    producer.produce(queue, json);
    producer.produce(queue, json1);
    producer.produce(queue, json2);
    producer.produce(queue, json3);
    ConsumeMsgResponse response = consumer.consume("queue");
    assertEquals(response.getQueueName(), "queue");
    assertEquals(response.getJsonObject(), json);
    assertEquals(response.getOffset(), 1);

    ConsumeMsgResponse response1 = consumer.consume("queue");
    assertEquals(response1.getJsonObject(), json1);
    assertEquals(response1.getOffset(), 2);

    ConsumeMsgResponse response2 = consumer.consume("queue");
    assertEquals(response2.getJsonObject(), json2);
    assertEquals(response2.getOffset(), 3);


    ConsumeMsgResponse response3 = consumer.consume("queue");
    assertEquals(response3.getJsonObject(), json3);
    assertEquals(response3.getOffset(), 4);


    // Another consumer consuming same messages produced
    LLQueueConsumer consumer1 = new LLQueueConsumer(metaDataService);
    ConsumeMsgResponse response4 = consumer1.consume("queue");
    assertEquals(response4.getJsonObject(), json);
    assertEquals(response4.getOffset(), 1);

    ConsumeMsgResponse response5 = consumer1.consume("queue");
    assertEquals(response5.getJsonObject(), json1);
    assertEquals(response5.getOffset(), 2);

    ConsumeMsgResponse response6 = consumer1.consume("queue");
    assertEquals(response6.getJsonObject(), json2);
    assertEquals(response6.getOffset(), 3);

    ConsumeMsgResponse response7 = consumer1.consume("queue");
    assertEquals(response7.getJsonObject(), json3);
    assertEquals(response7.getOffset(), 4);


    // Another consumer consuming same messages from offset
    LLQueueConsumer consumer2 = new LLQueueConsumer(metaDataService);
    ConsumeMsgResponse response8 = consumer2.consume("queue", 3);
    assertEquals(response8.getJsonObject(), json2);
    assertEquals(response8.getOffset(), 3);

    ConsumeMsgResponse response9 = consumer2.consume("queue");
    assertEquals(response9.getJsonObject(), json3);
    assertEquals(response9.getOffset(), 4);

    ConsumeMsgResponse response10 = consumer2.consume("queue");
    assertNull(response10.getJsonObject());

  }

  @Test
  void testOffsetReset() {
    MetaDataService metaDataService = new MetaDataService();
    LLQueueConsumer consumer = new LLQueueConsumer(metaDataService);
    LinkedListQueue queue = metaDataService.getOrCreateQueue("queue");
    LLQueueProducer producer = new LLQueueProducer();
    JSONObject json = new JSONObject(); json.put("key", "value");
    JSONObject json1 = new JSONObject(); json1.put("key", "value1");
    JSONObject json2 = new JSONObject(); json2.put("key", "value2");
    JSONObject json3 = new JSONObject(); json3.put("key", "value3");
    producer.produce(queue, json);
    producer.produce(queue, json1);
    ConsumeMsgResponse response = consumer.consume("queue");
    assertEquals(response.getQueueName(), "queue");
    assertEquals(response.getJsonObject(), json);
    assertEquals(response.getOffset(), 1);
    consumer.offsetReset("queue");
    ConsumeMsgResponse response1 = consumer.consume("queue");
    assertEquals(response1.getJsonObject(), json);
    assertEquals(response1.getOffset(), 1);
  }

}