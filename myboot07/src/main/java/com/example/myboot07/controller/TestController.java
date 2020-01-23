package com.example.myboot07.controller;


import com.example.myboot07.bean.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *  测试Controller
 */
@Controller
public class TestController {

    @RequestMapping("/testerror")
    public String testError(){

        return "loginError";
    }

    @ResponseBody
    @RequestMapping("/testAjax")
    public Object testAjax(String title){

        Result result = new Result();

        result.setSuccess(true);

        return result;
    }

    @RequestMapping("/to404")
    public String to404(){
        return "404";
    }

    @RequestMapping("/to500")
    public String to500(){
        return "500";
    }

    @RequestMapping("/tologinerror")
    public String tologinerror(){
        return "loginError";
    }

}
