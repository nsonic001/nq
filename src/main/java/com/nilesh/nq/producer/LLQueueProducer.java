package com.nilesh.nq.producer;

import com.nilesh.nq.queue.LinkedListQueue;
import org.json.simple.JSONObject;

public class LLQueueProducer implements NqProducer{
  @Override
  public void produce(LinkedListQueue queue, JSONObject jsonObject) {
    queue.enqueue(jsonObject);
  }
}
