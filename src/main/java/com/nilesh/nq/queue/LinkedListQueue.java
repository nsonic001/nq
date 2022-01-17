package com.nilesh.nq.queue;

import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONObject;

@Getter
@Setter
public class LinkedListQueue implements NQueue{

  String name;
  private long lastOffset = 0;

  public LinkedListQueue(String name){
    this.setName(name);
  }

  Node head, tail = null;
  // default max retention of 1 day
  long maxRetention = 86400000;

  //add a node to the list
  private void addNode(JSONObject item) {
    //Create a new node
    Node newNode = new Node(item);

    //if list is empty, head and tail points to newNode
    if(head == null) {
      newNode.offset = this.lastOffset+1;
      head = tail = newNode;
    }
    else {
      // Add newNode to front of the list
      newNode.offset = this.lastOffset+1;
      head.setPrevious(newNode);
      newNode.setNext(head);
      head = newNode;
    }
    this.lastOffset+=1;
    head.setPrevious(null);
  }

  private void removeOldNodes(long timestamp){
    while (tail.getTimestamp()<timestamp){
      Node temp = tail;
      tail = tail.getPrevious();
      temp.setPrevious(null);
      tail.setNext(null);
    }
  }

  @Override
  public void enqueue(JSONObject jsonObject) {
    this.addNode(jsonObject);
  }

  @Override
  public void dequeueOld(long timestamp) {
    this.removeOldNodes(timestamp);
  }

  @Override
  public Node readFromOffset(long offset) {
    if(head.offset<offset || tail.offset>offset){
      return null;
    }else{
      Node temp = tail;
      while(temp.offset<offset){
        temp = temp.getPrevious();
      }
      return temp;
    }
  }

  @Override
  public void updateMaxRetention(long retention) {
    this.maxRetention = retention;
  }
}
