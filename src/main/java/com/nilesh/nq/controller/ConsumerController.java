package com.nilesh.nq.controller;

import com.nilesh.nq.consumer.LLQueueConsumer;
import com.nilesh.nq.queue.LinkedListQueue;
import com.nilesh.nq.request.ConsumerRegisterRequest;
import com.nilesh.nq.response.ConsumeMsgResponse;
import com.nilesh.nq.response.ConsumerRegisterResponse;
import com.nilesh.nq.service.MetaDataService;
import javax.validation.Valid;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConsumerController {
  @Autowired
  private MetaDataService metaDataService;

  @RequestMapping(path = "/v1/consume/msg/{consumerId}/{queueName}", method = RequestMethod.GET)
  @Retryable(maxAttempts = 3)
  public ConsumeMsgResponse ConsumeMsg(@Valid @PathVariable String consumerId,
      @Valid @PathVariable String queueName){
    LLQueueConsumer consumer = metaDataService.getOrCreateConsumer(consumerId);
    ConsumeMsgResponse response = consumer.consume(queueName);
    response.setStatusCode(200);
    return response;
  }

  @RequestMapping(path = "/v1/consume/msg/{consumerId}/{queueName}/{offset}", method = RequestMethod.GET)
  @Retryable(maxAttempts = 3)
  public ConsumeMsgResponse ConsumeMsgFromOffset(@Valid @PathVariable String consumerId,
      @Valid @PathVariable String queueName, @Valid @PathVariable long offset){
    LLQueueConsumer consumer = metaDataService.getOrCreateConsumer(consumerId);
    ConsumeMsgResponse response = consumer.consume(queueName, offset);
    response.setStatusCode(200);
    return response;
  }

  @RequestMapping(path = "/v1/consumer/register", method = RequestMethod.POST)
  @Retryable(maxAttempts = 3)
  public ConsumerRegisterResponse ConsumerCallbackRegister(
      @Valid @RequestBody ConsumerRegisterRequest consumerRegisterRequest){

    metaDataService.registerConsumer(consumerRegisterRequest.getQueueName(),
        consumerRegisterRequest.getConsumerName(),
        consumerRegisterRequest.getCallbackURL());

    return ConsumerRegisterResponse.builder().registered(true).build();
  }
}
