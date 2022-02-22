package com.ewy.activiti7.util;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class PathMapping implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("classpath:/resources/");// 默认映射
        registry.addResourceHandler("/bpmn/**").addResourceLocations(GlobalConfig.BPMN_PathMapping);
    }
}

////扩展spring mvc组件
//@Configuration
//public class MyWebMvcConfigurer implements WebMvcConfigurer {
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//
//        //获取当前操作系统
//        String osName=System.getProperty("os.name");
//        //如果是windows
//        if (osName.toLowerCase().startsWith("win")){
//            registry.addResourceHandler("/upload/**")
//                    .addResourceLocations("file:D:/community/upload/");
//        }else {
//            //linux或者mac
//            registry.addResourceHandler("/upload/**")
//                    .addResourceLocations("file:/resources/community/upload/");
//        }
//    }
//}
