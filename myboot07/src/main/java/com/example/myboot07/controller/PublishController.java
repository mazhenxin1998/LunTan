package com.example.myboot07.controller;

import com.example.myboot07.bean.Tag;
import com.example.myboot07.service.ITagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.LinkedList;
import java.util.List;

@Controller
public class PublishController {

    @Autowired
    private ITagService tagService;

    /**
     *  由于第一次进入页面会直接显示错误信息  则在这里对该错误信息进行处理
     * @param request
     * @return
     */

    @GetMapping("/publish")
    public String publishController(HttpServletRequest request, Model model){
        request.setAttribute("result",null);
        request.setAttribute("title","");
        request.setAttribute("description","");

        // 查出所有的Tag 并放入到session域中
        List<Tag> tagAll = tagService.queryTagAll();

        // 将前端标签分类
        List<Tag> frontDevelopment = new LinkedList<Tag>();
        // 将后端标签分类
        List<Tag> backgroundDevelopment = new LinkedList<Tag>();
        // 将语言分类
        List<Tag> languageDevelopment = new LinkedList<Tag>();
        // 将大数据分类
        List<Tag> bigData = new LinkedList<Tag>();
        if( tagAll == null ){
                // 错误
        }else{
             //  正常     将tag标签分类
            for (Tag tag : tagAll) {
                if(tag.getType().equals("开发语言")){
                    languageDevelopment.add(tag);
                }

                if(tag.getType().equals("前端开发")){
                    frontDevelopment.add(tag);
                }

                if(tag.getType().equals("后端开发")){
                    backgroundDevelopment.add(tag);
                }

                if(tag.getType().equals("大数据")){
                    bigData.add(tag);
                }

            }

            HttpSession session = request.getSession();
            model.addAttribute("frontDevelopment",frontDevelopment);
            model.addAttribute("languageDevelopment",languageDevelopment);
            model.addAttribute("backgroundDevelopment",backgroundDevelopment);
            model.addAttribute("bigData",bigData);
            model.addAttribute("tagAll",tagAll);

        }

        return "publish";
    }

}
