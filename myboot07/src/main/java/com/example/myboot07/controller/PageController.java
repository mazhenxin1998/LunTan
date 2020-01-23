package com.example.myboot07.controller;

import com.example.myboot07.bean.AjaxResult;
import com.example.myboot07.bean.User;
import com.example.myboot07.dto.QuestionDTO;
import com.example.myboot07.service.IQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 *   分页控制器
 */
@Controller
public class PageController {

    /**
     *  分页控制器
     *
     *     声明全局变量
     */

    @Autowired
    private IQuestionService questionService;

    @ResponseBody
    @RequestMapping("/dopage")
    public Object queryPage(@RequestParam(value = "pageno",required = false,defaultValue = "1") Integer pageno,
                            @RequestParam(value = "pagesize",required = false,defaultValue = "7") Integer pagesize){

        AjaxResult result = new AjaxResult();

        try {
            // 返回前六条数据
            List<QuestionDTO> list = questionService.queryQuestionDtoAll(pageno,pagesize);
            result.setDatas(list);

            result.setSuccess(true);

        }catch (Exception e){
            result.setSuccess(false);
        }
        return result;
    }

}
