package com.example.myboot07.controller;

import com.example.myboot07.bean.AjaxResult;
import com.example.myboot07.bean.Result;
import com.example.myboot07.bean.User;
import com.example.myboot07.bean.UserRegister;
import com.example.myboot07.config.MD5Encription;
import com.example.myboot07.service.ILoginService;
import com.example.myboot07.service.IUserService;
import org.omg.PortableInterceptor.USER_EXCEPTION;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
public class LoginAndRegisterController {

    /**
     *
     */
    @Autowired
    private ILoginService loginService;

    @Autowired
    private IUserService userService;


    /**
     * 处理登录请求---同步
     *
     *          需要将当前登录的对象放入到session域中
     *
     */
    @ResponseBody
    @PostMapping("/dealWithLogin")
    public Object dealWithLogin(String account, String password,
                                HttpSession session){

        Result result = new Result();

       // 校验----根据account

        User user = userService.queryUserByAccount(account);

        // 校验之前 应该将password密码进行MD5加密
        String newPassword = MD5Encription.digest(password);

        if(account.equals(user.getAccount())&&newPassword.equals(user.getPassword())){
            // 校验通过--则将该user对象放入到session域中
            session.setAttribute("user",user);
            result.setSuccess(true);
        }else{
            result.setSuccess(false);
        }

        return result;
    }


    /**
     *    处理注册请求
     */
    @ResponseBody   //  异步处理
    @RequestMapping("/dealWithRegister")
    public Object dealWithRegister(String account ,String password ,String name ){

        AjaxResult result = new AjaxResult();

        // 将所有要增加的数据封装到一个user对象中
        User user = new User();
        user.setAccount(account);

        // 设置密码的时候  需要将密码加密
        String newPassword = MD5Encription.digest(password);
        user.setPassword(newPassword);
        user.setName(name);

        // 添加创建时间
        String nowTime = new Date().toLocaleString();
        user.setGmt_creat(nowTime);

        // 设置默认头像
        user.setAvatar_url("http://kaixin-boke.cn-bj.ufileos.com/583ca948-57c3-44c0-b4d7-8e3fce4fc803.png?UCloudPublicKey=TOKEN_099dc871-5117-4fb4-a669-7785e4cd7805&Signature=2RKa1aB60rqY5fzPh9Msg%2BeltTE%3D&Expires=3402922236");

        // 保存用户之前应该确定该用户是没有被注册过的----默认账号是不能重复的
        User userOld = userService.queryUserByAccount(user.getAccount());

        if(userOld == null){
            // 说明当前账户为空  可以进行注册

            Integer count = userService.saveUserByRegister(user);

            if(count == 1){
                result.setSuccess(true);
            }else{
                result.setSuccess(false);
            }

        }else{
            // 不能注册
            result.setCode(2013);  //  2013 表示当前注册的账号已经存在了
            result.setSuccess(false);
        }

        return result;
    }

}
