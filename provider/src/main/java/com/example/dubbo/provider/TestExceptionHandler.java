package com.example.dubbo.provider;

import com.example.dubbo.api.exception.TraceableRuntimeException;
import org.apache.dubbo.rpc.RpcContext;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Description:
 *
 * @author: lizhi1
 * @date: 2019-05-07
 */
@ControllerAdvice(basePackages = "com.example.dubbo.provider")
public class TestExceptionHandler {

  @ExceptionHandler(Exception.class)
  public String exceptionHandler(Exception e) throws Exception {
    if (e instanceof TraceableRuntimeException) {
      throw e;
    }

    throw new TraceableRuntimeException(e.getMessage(), e,
        RpcContext.getContext().getAttachment("traceId"));
  }
}
