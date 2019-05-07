package com.example.dubbo.api.storage;

import java.util.UUID;

/**
 * Description:
 *
 * @author: lizhi1
 * @date: 2019-05-07
 */
public class TraceThreadLocal {

  private String traceId;

  private static ThreadLocal<TraceThreadLocal> threadLocal = new ThreadLocal<>();

  public static void init(String traceId) {
    TraceThreadLocal traceThreadLocal = new TraceThreadLocal();
    traceThreadLocal.traceId = traceId;
    threadLocal.set(traceThreadLocal);
    System.out.println(
        "--- generate and set trace id " + traceId + " to thread local"
            + "\n---thread name " + Thread.currentThread().getName());
  }

  public static void init() {
    String traceId = UUID.randomUUID().toString().replaceAll("-", "");
    TraceThreadLocal.init(traceId);
  }

  public static TraceThreadLocal get() {
    return threadLocal.get();
  }

  public static void clear() {
    threadLocal.remove();
  }

  public String getTraceId() {
    return traceId;
  }

  public void setTraceId(String traceId) {
    this.traceId = traceId;
  }
}
