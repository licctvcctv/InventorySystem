package com.example.roleservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.roleservice.mapper")
public class RoleServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(RoleServiceApplication.class, args);
    }
}
