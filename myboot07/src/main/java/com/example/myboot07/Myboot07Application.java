package com.example.myboot07;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("com.example.myboot07.dao")
@EnableTransactionManagement
public class Myboot07Application {

    public static void main(String[] args) {
        SpringApplication.run(Myboot07Application.class, args);
    }

}
