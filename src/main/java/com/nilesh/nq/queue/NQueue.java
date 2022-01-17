package com.nilesh.nq.queue;

import org.json.simple.JSONObject;
public interface NQueue {

  void enqueue(JSONObject jsonObject);
  void dequeueOld(long timestamp);
  void updateMaxRetention(long retention);
  Node readFromOffset(long offset);

}
