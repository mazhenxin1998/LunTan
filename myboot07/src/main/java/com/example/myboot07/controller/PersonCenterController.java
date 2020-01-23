package com.example.myboot07.controller;

import com.example.myboot07.bean.Result;
import com.example.myboot07.bean.User;
import com.example.myboot07.bean.UserInfo;
import com.example.myboot07.service.IUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * @author lenovo
 */
@Controller
public class PersonCenterController {

    @Autowired
    private IUserInfoService userInfoService;

    /**
     *
     * @return
     */
    @GetMapping("/toPersonCenter")
    public String toPersonCenter(HttpSession session, Model model){
        User user = (User) session.getAttribute("user");
        // 前往个人中心 但是如果用户未登录,那么就提示让user登录

        UserInfo userInfo = userInfoService.queryUserInfoByUserId(user.getId());
        session.setAttribute("userinfo",userInfo);
        return "PersonCenter";
    }

    @RequestMapping("/updateUserinfo")
    @ResponseBody
    public Object updateUserinfo(UserInfo userInfo,HttpSession session){


        Result result = new Result();
        User user = (User) session.getAttribute("user");
        userInfo.setUserid(user.getId());

        Integer count = 0;
        UserInfo userInfo1 = userInfoService.queryUserInfoByUserId(user.getId());
        if(userInfo1 == null){
         count = userInfoService.insertUserInfo(userInfo);
        }else{
            // user信息已经存在   那么就更新
            userInfo.setId(userInfo1.getId());
            userInfoService.updateUserInfoByUserInfo(userInfo);
            result.setCode(2018);
        }

        if(count == 1 ){
            result.setSuccess(true);
            // 对session里面的同一个值进行修改
            session.setAttribute("userinfo",userInfo1);
        }else{
            result.setSuccess(false);
        }




        return result;

    }




}
