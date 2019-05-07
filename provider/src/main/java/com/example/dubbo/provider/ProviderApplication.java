package com.example.dubbo.provider;

import com.example.dubbo.api.exception.TraceableRuntimeException;
import com.example.dubbo.provider.utils.EmbeddedZooKeeper;
import org.apache.dubbo.rpc.RpcContext;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@SpringBootApplication
@Configuration
@ImportResource("classpath:/dubbo-provider.xml")
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
