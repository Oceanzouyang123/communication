package com.mybatis.util;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class SimpleWebAppConfigure extends WebMvcConfigurerAdapter {
 /* @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    //registry.addResourceHandler("/myresource/**").addResourceLocations("classpath:/myresource/");
    // 可以直接使用addResourceLocations 指定磁盘绝对路径，同样可以配置多个位置，注意路径写法需要加上file:
    registry.addResourceHandler("/myimgs/**").addResourceLocations("file:C:/Users/01375042/AppData/Local/Temp/msohtmlclip1/01");
    super.addResourceHandlers(registry);
  }*/
}
