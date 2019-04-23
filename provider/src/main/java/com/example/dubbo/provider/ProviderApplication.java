package com.example.dubbo.provider;

import com.example.dubbo.provider.utils.EmbeddedZooKeeper;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class ProviderApplication {

  public static void main(String[] args) {
    new SpringApplicationBuilder(ProviderApplication.class)
        .listeners((ApplicationListener<ApplicationEnvironmentPreparedEvent>) event -> {
          Environment environment = event.getEnvironment();
          Integer port = environment.getProperty("embedded.zookeeper.port", Integer.class);
          new EmbeddedZooKeeper(port, false).start();
        }).run(args);
  }

}
