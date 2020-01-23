package com.example.myboot07.intercepter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

/**
 * 配置自定义拦截器
 * 注解@EnableWebMvc 注解： 如果该注解下的类实现了WebMvcConfigurer  接口 那么就说明有关springmvc的所有配置都会失效
 * 如果要继续使用springmvc的功能，那么就必须实现所有的WebMvcConfigure 的方法
 */

@Configuration
public class MyWeb implements WebMvcConfigurer {

    @Autowired
    private MyInterceptor myInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 1. 通过registry 来向当前IOC容器中增加拦截器
        //  所有路径都访问该拦截器
        registry.addInterceptor(myInterceptor).addPathPatterns("/**").excludePathPatterns("https://github.com/login/oauth/authorize?client_id=162575788251fb2fb193&redirect=http://localhost:8080/callback&scope=user&state=1");
        registry.addInterceptor(myInterceptor).excludePathPatterns("https://github.com/login/oauth/access_token");


        // registry  注册的意思
        // 可以用该注解  重写springmvc的默认配置
    }

}
