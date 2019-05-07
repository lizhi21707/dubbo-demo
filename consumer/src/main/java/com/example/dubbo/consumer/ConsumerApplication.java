package com.example.dubbo.consumer;

import com.example.dubbo.api.aspect.ControllerRequestTraceAspectj;
import com.example.dubbo.api.exception.TraceableRuntimeException;
import com.example.dubbo.api.interceptor.TraceFilter;
import com.example.dubbo.api.storage.TraceThreadLocal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication(
    scanBasePackages = {
        "com.example.dubbo.api",
        "com.example.dubbo.consumer"})
@Configuration
@ImportResource("classpath:/dubbo-consumer.xml")
@ControllerAdvice(basePackages = "com.example.dubbo.consumer")
public class ConsumerApplication implements WebMvcConfigurer {

  public static void main(String[] args) {
    SpringApplication.run(ConsumerApplication.class, args);
  }

  @ExceptionHandler
  public String exceptionHandler(Exception e) {
    System.out.println(e.getMessage());
    if (e instanceof TraceableRuntimeException) {
      System.out.println("traceId:" + TraceThreadLocal.get().getTraceId());
    }
    return "errorMe";
  }

  @Bean
  public FilterRegistrationBean<TraceFilter> traceFilter() {
    FilterRegistrationBean<TraceFilter> registrationBean = new FilterRegistrationBean<>();
    registrationBean.setFilter(new TraceFilter());
    List<String> urlPatterns = new ArrayList<>();
    urlPatterns.add("/*");
    registrationBean.setUrlPatterns(urlPatterns);
    registrationBean.setOrder(1);
    return registrationBean;
  }

  @Bean
  public ControllerRequestTraceAspectj traceAspectj() {
    return new ControllerRequestTraceAspectj();
  }
}
