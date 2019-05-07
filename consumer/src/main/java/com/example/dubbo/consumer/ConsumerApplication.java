package com.example.dubbo.consumer;

import com.example.dubbo.api.exception.TraceableRuntimeException;
import org.apache.dubbo.rpc.RpcContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@SpringBootApplication(
    scanBasePackages = {
        "com.example.dubbo.api",
        "com.example.dubbo.consumer"})
@Configuration
@ImportResource("classpath:/dubbo-consumer.xml")
@ControllerAdvice(basePackages = "com.example.dubbo.consumer")
public class ConsumerApplication {

  public static void main(String[] args) {
    SpringApplication.run(ConsumerApplication.class, args);
  }

  @ExceptionHandler
  public String exceptionHandler(Exception e) {
    System.out.println(e.getMessage());
    if (e instanceof TraceableRuntimeException) {
      System.out.println("traceId:" + ((TraceableRuntimeException) e).getTraceId());
    }
    return "errorMe";
  }
}
