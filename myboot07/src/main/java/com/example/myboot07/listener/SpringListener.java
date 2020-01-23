package com.example.myboot07.listener;


import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Spring自定义监听器
 */
//@Component
public class SpringListener implements ApplicationListener<ApplicationEvent> {

    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        //  获取该项目的上下文
        ServletContext application = (ServletContext) applicationEvent.getSource();
        String path = application.getContextPath();
        HttpSession session = (HttpSession) applicationEvent.getSource();
        session.setAttribute("APP_PATH",path);
        System.out.println("上下文"+path);



    }


}
