package com.example.myboot07.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *  退出登录
 *       1.  消除session
 *          方法： 重命名为空 并且设置最大时间为0
 */
@Controller
public class LogOutController {

    @RequestMapping("/logout")
    public String logout(HttpServletRequest request){

        HttpSession session = request.getSession();

        session.removeAttribute("unreadSize");
        session.removeAttribute("notifiCations");
        session.removeAttribute("notificationQuestion");
        session.removeAttribute("notificationComment");

        session.removeAttribute("user");

        return "redirect:/";
    }


}
