package com.nilesh.nq.config;

import com.nilesh.nq.service.MetaDataService;
import java.util.concurrent.Future;
import org.apache.catalina.core.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

@Component
public class AsyncExecutor {


  @Async
  public Future<String> asyncMethodWithReturnType(MetaDataService metaDataService) {

    System.out.println("Execute method asynchronously - "
        + Thread.currentThread().getName());
    metaDataService.queueCleanup();
    return new AsyncResult<String>("queue cleanup done");
  }

}
