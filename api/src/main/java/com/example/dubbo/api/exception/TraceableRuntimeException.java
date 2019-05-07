package com.example.dubbo.api.exception;

/**
 * Description:
 *
 * @author: lizhi1
 * @date: 2019-05-07
 */
public class TraceableRuntimeException extends RuntimeException {

  private String traceId;

  public TraceableRuntimeException(String message) {
    super(message);
  }

  public TraceableRuntimeException(String message, String traceId) {
    super(message);
    this.traceId = traceId;
  }

  public TraceableRuntimeException(String message, Throwable cause, String traceId) {
    super(message, cause);
    this.traceId = traceId;
  }

  public String getTraceId() {
    return traceId;
  }

  public void setTraceId(String traceId) {
    this.traceId = traceId;
  }
}
