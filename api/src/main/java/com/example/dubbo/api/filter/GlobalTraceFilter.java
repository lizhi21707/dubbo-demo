package com.example.dubbo.api.filter;

import com.example.dubbo.api.exception.TraceableRuntimeException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.reflect.Method;
import java.util.UUID;
import org.apache.dubbo.common.Constants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.common.utils.StringUtils;
import org.apache.dubbo.rpc.Filter;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.RpcException;
import org.apache.dubbo.rpc.RpcResult;
import org.apache.dubbo.rpc.service.GenericService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description:
 *
 * @author: lizhi1
 * @date: 2019-05-07
 */
@Activate(group = {Constants.CONSUMER, Constants.PROVIDER}, order = -9999)
public class GlobalTraceFilter implements Filter {

  private static final ObjectMapper JSON_MAPPER = new ObjectMapper();

  private Logger logger = LoggerFactory.getLogger(GlobalTraceFilter.class);

  @Override
  public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
    String traceId = invocation.getAttachment("traceId");
    if (!StringUtils.isBlank(traceId)) {
      RpcContext.getContext().setAttachment("traceId", traceId);
    } else {
      // 第一次发起调用
      RpcContext.getContext().setAttachment("traceId", UUID.randomUUID().toString());
    }

    // return invoker.invoke(invocation);
    try {
      Result result = invoker.invoke(invocation);
      if (result.hasException() && GenericService.class != invoker.getInterface()) {
        try {
          Throwable exception = result.getException();

          // 如果是checked异常，直接抛出
          if (!(exception instanceof RuntimeException)
              && (exception instanceof Exception)) {
            return result;
          }

          // 在方法签名上有声明，直接抛出
          try {
            Method method = invoker.getInterface()
                .getMethod(invocation.getMethodName(), invocation.getParameterTypes());
            Class<?>[] exceptionClassses = method.getExceptionTypes();
            for (Class<?> exceptionClass : exceptionClassses) {
              if (exception.getClass().equals(exceptionClass)) {
                return result;
              }
            }
          } catch (NoSuchMethodException e) {
            return result;
          }

          // 未在方法签名上定义的异常，在服务器端打印ERROR日志
          logger.error("Got unchecked and undeclared exception which called by "
              + RpcContext.getContext().getRemoteHost()
              + ". service: " + invoker.getInterface().getName()
              + ", method: " + invocation.getMethodName()
              + ", methodArgs: " + JSON_MAPPER.writeValueAsString(invocation.getArguments())
              + ", exception: " + exception.getClass().getName()
              + ": " + exception.getMessage(), exception);

          // 异常类和接口类在同一jar包里，直接抛出
          // String serviceFile = ReflectUtils.getCodeBase(invoker.getInterface());
          // String exceptionFile = ReflectUtils.getCodeBase(exception.getClass());
          // if (serviceFile == null || exceptionFile == null || serviceFile.equals(exceptionFile)) {
          //   return result;
          // }
          // 是JDK自带的异常，直接抛出
          String className = exception.getClass().getName();
          if (className.startsWith("java.") || className.startsWith("javax.")) {
            return result;
          }
          // 是Dubbo本身的异常，直接抛出
          if (exception instanceof RpcException) {
            return result;
          }

          // 是Edu系列的异常，直接抛出
          if (exception instanceof TraceableRuntimeException) {
            ((TraceableRuntimeException) exception).setTraceId(traceId);
            return result;
          }

          // 否则，包装成RuntimeException抛给客户端
          return new RpcResult(new RuntimeException(StringUtils.toString(exception)));
        } catch (Exception e) {
          logger.warn("Fail to ExceptionFilter when called by "
              + RpcContext.getContext().getRemoteHost()
              + ". service: " + invoker.getInterface().getName()
              + ", method: " + invocation.getMethodName()
              + ", exception: " + e.getClass().getName() + ": " + e.getMessage(), e);
          return result;
        }
      }
      return result;
    } catch (RuntimeException e) {
      logger.error("Got unchecked and undeclared exception which called by "
          + RpcContext.getContext().getRemoteHost()
          + ". service: " + invoker.getInterface().getName()
          + ", method: " + invocation.getMethodName()
          + ", exception: " + e.getClass().getName() + ": " + e.getMessage(), e);
      throw e;
    }
  }
}

