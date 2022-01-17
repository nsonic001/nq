package com.nilesh.nq.consumer;

import com.nilesh.nq.queue.LinkedListQueue;
import com.nilesh.nq.queue.Node;
import com.nilesh.nq.response.ConsumeMsgResponse;
import org.json.simple.JSONObject;

public interface NqConsumer {

  ConsumeMsgResponse consume(String queueName);
  ConsumeMsgResponse consume(String queueName, long offset);
  void offsetReset(String queueName);
}
