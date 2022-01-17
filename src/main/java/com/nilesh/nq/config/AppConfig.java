package com.nilesh.nq.config;

import com.nilesh.nq.service.MetaDataService;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jobrunr.jobs.mappers.JobMapper;
import org.jobrunr.scheduling.JobScheduler;
import org.jobrunr.storage.InMemoryStorageProvider;
import org.jobrunr.storage.StorageProvider;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;
import org.springframework.retry.annotation.EnableRetry;

@Getter
@Setter
@Validated
@ToString
@EnableRetry
@ConfigurationProperties("app")
public class AppConfig {
  private MetaDataService metaDataService;
  private JobScheduler jobScheduler;

  @Bean
  public MetaDataService getMetaDataService(){
    return this.metaDataService;
  }

  @Bean
  public JobScheduler getJobScheduler(){
    return this.jobScheduler;
  }

  @Bean
  public StorageProvider storageProvider(JobMapper jobMapper) {
    InMemoryStorageProvider storageProvider = new InMemoryStorageProvider();
    storageProvider.setJobMapper(jobMapper);
    return storageProvider;
  }


}
