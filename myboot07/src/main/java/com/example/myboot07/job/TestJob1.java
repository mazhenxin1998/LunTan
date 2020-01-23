package com.example.myboot07.job;



import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@EnableScheduling
public class TestJob1  {

    public void test(){
        System.out.println("方法执行了:"+new Date().toLocaleString());
    }



}
