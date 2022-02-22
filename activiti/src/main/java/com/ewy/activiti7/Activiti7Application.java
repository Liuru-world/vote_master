package com.ewy.activiti7;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(value = "com.ewy.activiti7.mapper")
public class Activiti7Application {
    public static void main(String[] args) {
        SpringApplication.run(Activiti7Application.class, args);
    }

}
