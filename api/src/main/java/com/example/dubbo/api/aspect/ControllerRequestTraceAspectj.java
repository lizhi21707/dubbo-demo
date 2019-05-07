package com.example.dubbo.api.aspect;

import com.example.dubbo.api.response.ResponseDTO;
import com.example.dubbo.api.storage.TraceThreadLocal;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Description:
 *
 * @author: lizhi1@corp.netease.com
 * @date: 2018/08/15
 */
@Aspect
// @Component
public class ControllerRequestTraceAspectj {

  @Pointcut(value = "execution(public * com.example.dubbo.*.*Controller.*(..))")
  public void controllerPontCut() {
  }

  @AfterReturning(pointcut = "controllerPontCut()", returning = "response")
  public ResponseDTO controllerTraceFill(ResponseDTO response) {
    if (TraceThreadLocal.get() != null) {
      response.setTraceId(TraceThreadLocal.get().getTraceId());
    }
    return response;
  }

}
