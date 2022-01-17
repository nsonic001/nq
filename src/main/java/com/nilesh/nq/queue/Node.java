package com.nilesh.nq.queue;

import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONObject;

@Getter
@Setter
public class Node {

  JSONObject item;
  long offset;
  private long timestamp;
  private Node previous;
  private Node next;

  public Node(JSONObject item) {
    this.item = item;
    this.timestamp = System.currentTimeMillis();
  }
}
