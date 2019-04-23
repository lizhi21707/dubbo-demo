package com.example.dubbo.provider;

import com.example.dubbo.api.DemoApi;
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
    return "Hello World!";
  }
}
