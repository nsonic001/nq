package com.nilesh.nq.controller;

import com.nilesh.nq.producer.LLQueueProducer;
import com.nilesh.nq.queue.LinkedListQueue;
import com.nilesh.nq.request.AddToQueueRequest;
import com.nilesh.nq.response.AddToQueueResponse;
import com.nilesh.nq.service.MetaDataService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProducerController {
  @Autowired
  private MetaDataService metaDataService;

  @RequestMapping(path = "/v1/produce/msg", method = RequestMethod.PUT)
  @Retryable(maxAttempts = 3)
  public AddToQueueResponse AddToQueue(@Valid @RequestBody AddToQueueRequest addToQueueRequest){
    LinkedListQueue queue = metaDataService.getOrCreateQueue(addToQueueRequest.getQueueName());
    LLQueueProducer producer = new LLQueueProducer();
    producer.produce(queue, addToQueueRequest.getJsonObject());
    return AddToQueueResponse.builder().offset(queue.getHead().getOffset())
        .queueName(addToQueueRequest.getQueueName()).statusCode(200).build();
  }

}
