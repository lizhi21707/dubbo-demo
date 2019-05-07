package com.example.dubbo.api.interceptor;

import com.example.dubbo.api.storage.TraceThreadLocal;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * Description:
 *
 * @author: lizhi1
 * @date: 2019-05-07
 */
public class TraceGlobalRequestFilter implements Filter {

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {

  }

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
      FilterChain filterChain) throws IOException, ServletException {
    try {
      TraceThreadLocal.init();
      filterChain.doFilter(servletRequest, servletResponse);
    } finally {
      TraceThreadLocal.clear();
    }
  }

  @Override
  public void destroy() {
  }
}
