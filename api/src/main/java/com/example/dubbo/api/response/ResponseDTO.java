package com.example.dubbo.api.response;

/**
 * Description:
 *
 * @author: lizhi1
 * @date: 2019-05-07
 */
public class ResponseDTO<T> {

  private String traceId;
  private T result;

  public String getTraceId() {
    return traceId;
  }

  public void setTraceId(String traceId) {
    this.traceId = traceId;
  }

  public T getResult() {
    return result;
  }

  public void setResult(T result) {
    this.result = result;
  }
}
