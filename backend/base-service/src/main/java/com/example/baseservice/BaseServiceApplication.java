package com.example.baseservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@MapperScan("com.example.baseservice.mapper")
@EnableDiscoveryClient
@EnableFeignClients
public class BaseServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(BaseServiceApplication.class, args);
    }
}
