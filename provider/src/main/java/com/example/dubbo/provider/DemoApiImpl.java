package com.example.dubbo.provider;

import com.example.dubbo.api.DemoApi;
import com.example.dubbo.api.exception.TraceableRuntimeException;
import org.apache.dubbo.rpc.RpcContext;
import org.springframework.stereotype.Service;

/**
 * Description:
 *
 * @author: lizhi1
 * @date: 2019-04-23
 */
//@Service(version = "1.0.0")
@Service("demoApi")
public class DemoApiImpl implements DemoApi {

  @Override
  public String sayHello() {
    System.out.println("****** demo provider 被访问 ******");

    String traceId = RpcContext.getContext().getAttachment("traceId");
    System.out.println("traceId:" + traceId);

    // return "Hello World!";
    throw new TraceableRuntimeException("aaaaaa");
  }

  @Override
  public String sayHello(String message) {
    System.out.println("****** demo provider 被访问 ******");

    String traceId = RpcContext.getContext().getAttachment("traceId");
    System.out.println("traceId:" + traceId);

    // return "Hello World!";
    throw new TraceableRuntimeException("aaaaaa");
  }

}
