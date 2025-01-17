package com.agile.userapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.agile.userapp")
public class UserappApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserappApplication.class, args);
    }
}
