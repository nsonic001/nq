package com.nilesh.nq.producer;

import com.nilesh.nq.queue.LinkedListQueue;
import org.json.simple.JSONObject;

public interface NqProducer {

  void produce(LinkedListQueue queue, JSONObject jsonObject);
}
