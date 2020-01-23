package com.example.myboot07.controller;

import com.example.myboot07.bean.NotifiCation;
import com.example.myboot07.bean.Result;
import com.example.myboot07.bean.User;
import com.example.myboot07.service.INotifiCationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class NotifiCationController {

    @Autowired
    private INotifiCationService notifiCationService;

    @ResponseBody
    @RequestMapping("/notification")
    public Object notifiCation(HttpSession session){

        Result result = new Result();

        User user = (User) session.getAttribute("user");

        // 根据List查出所有的status为0的所有通知
        //List<NotifiCation> lists = notifiCationService.queryUnreadNotifiCationByReciver(user.getId());


        return result;
    }




}
