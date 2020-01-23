package com.example.myboot07.listener;

import org.springframework.stereotype.Component;
import sun.security.krb5.internal.PAData;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;

/**
 *    监听器
 */
@WebListener
@Component
public class ServletWebListener implements ServletContextListener {

    public void contextInitialized(ServletContextEvent sce,HttpSession session) {

        ServletContext application = sce.getServletContext();
        String path =  application.getContextPath();
        session.setAttribute("APP_PATH",path);
    }
}
