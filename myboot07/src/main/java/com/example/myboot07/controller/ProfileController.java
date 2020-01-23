package com.example.myboot07.controller;

import com.example.myboot07.bean.Page;
import com.example.myboot07.bean.Quession;
import com.example.myboot07.dto.QuestionDTO;
import com.example.myboot07.service.IQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 *  用户我的问题中心controller
 */
@Controller
public class ProfileController {

    /**
     *  声明全局变量
     */
    @Autowired
    private IQuestionService questionService;

    /**
     *   跳转到个人中心
     */
    @RequestMapping("/myprofile")
    public String profile(Model model){
        model.addAttribute("mytitle","我的提问");
        return "profile";
    }


    /**
     *  处理我的问题请求
     *          1. 默认会传过来一个相同的参数 根据该参数来确定是哪个a连接起作用了
     */
    @RequestMapping("/domyquestion")
    public String doMyQuestion(Model model,
                               @RequestParam(value = "profileName",required = true ,defaultValue = "我的问题") String profileName,
                               @RequestParam(value = "pageno",required = false,defaultValue = "1") Integer pageno,
                               @RequestParam(value = "pagesize",required = false,defaultValue = "7") Integer pagesize){

        // 1.  需要在前台对相同的数据 进行不同的处理  ：  不同的请求 显示不同的内容
        // 2. myquestion:  代表我的问题页面      myreply :  代表最新回复
        // 3.  默认是: 我的提问
        if("myquestion".equals(profileName)){
            model.addAttribute("mytitle","我的提问");
        }

        if("myreply".equals(profileName)){
            model.addAttribute("mytitle","最新回复");
        }

        //  对数据 进行处理
        // 前提必须知道当前用户拥有几天问题
        List<Quession> quessionList = questionService.queryQuestionAll();

        // number 数量为总的页码数
        int number = quessionList.size()%7 ==0?quessionList.size()/7:quessionList.size()/7+1;

        if(pageno < 1){
            pageno = 1;
        }

        if(pageno > number){
            pageno = number;
        }

        // 给page封装属性
        Page page = new Page(pageno,pagesize);

        //  设置总的问题 那么当前页码也会算出来
        page.setTotalsize(quessionList.size());

        //  处理完之后  返回结果为6天 与页码有关
        List<QuestionDTO> list = questionService.queryQuestionDtoAll(pageno,pagesize);

        page.setList(list);

        model.addAttribute("pages",page);

        return "profile";
    }


}
