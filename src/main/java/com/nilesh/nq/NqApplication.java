package com.nilesh.nq;

import com.nilesh.nq.config.AppConfig;
import com.nilesh.nq.config.AsyncExecutor;
import com.nilesh.nq.service.MetaDataService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import org.apache.catalina.core.ApplicationContext;
import org.jobrunr.jobs.mappers.JobMapper;
import org.jobrunr.scheduling.JobScheduler;
import org.jobrunr.scheduling.cron.Cron;
import org.jobrunr.storage.InMemoryStorageProvider;
import org.jobrunr.storage.StorageProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@EnableConfigurationProperties(AppConfig.class)
@SpringBootApplication
public class NqApplication {


  public static void main(String[] args) throws InterruptedException, ExecutionException {
    SpringApplication.run(NqApplication.class, args);
/// TODO: AsyncExuector for Queue Cleanup For expiry based
//    AsyncExecutor asyncExecutor = new AsyncExecutor();
//
//    while (true) {
//      Future<String> future = asyncExecutor.asyncMethodWithReturnType(context.getBean(MetaDataService.class));
//      if (future.isDone()) {
//        System.out.println("Result from asynchronous process - " + future.get());
//      }
//      System.out.println("Continue doing something else. ");
//      Thread.sleep(1000);
//    }
  }



}
