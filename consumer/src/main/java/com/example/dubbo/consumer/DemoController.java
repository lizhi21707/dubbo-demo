package com.example.dubbo.consumer;

import com.example.dubbo.api.DemoApi;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description:
 *
 * @author: lizhi1
 * @date: 2019-04-23
 */
@RestController
public class DemoController {

  private static final Logger logger = LoggerFactory.getLogger(DemoController.class);

  //但是version一定要指定 不然会找不到服务 直连需要加url="dubbo://localhost:12345"，端口号和配置文件中保持一致
  @Reference(version = "1.0.0")
  private DemoApi demoApi;

  @GetMapping("/hello")
  public void sayHello() {
    System.out.println("****** demo consumer 被访问 ******");
    System.out.println(demoApi.sayHello());
  }
}
