package com.example.myboot07.controller;

import com.example.myboot07.bean.FileTag;
import com.example.myboot07.bean.Result;
import com.example.myboot07.bean.User;
import com.example.myboot07.provide.UCloudProvice;
import com.example.myboot07.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author lenovo
 */
@Controller
public class FileimagesController {

    @Autowired
    private UCloudProvice uCloudProvice;

    @Autowired
    private IUserService userService;




    /**
     *   该方法是对markdown来上传图片的
     *
     * @param request
     * @return
     */
    @RequestMapping("/fileUpdate")
    @ResponseBody
    public FileTag update(HttpServletRequest request, HttpSession session){

        //  上传文件常用对象
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        String name = "editormd-image-file";
        //   获取到要上传的对象
        MultipartFile file = multipartRequest.getFile(name);
        try {
            InputStream inputStream = file.getInputStream();
            //  获取文件原生名字 和该文件是什么类型 getContentType  返回该文件是什么类型的
            String fileName = file.getOriginalFilename();
            String mimeType = file.getContentType();

            //  调用UCloud接口  该path就是上传图片的URL地址 同时也应该更新数据库的字段
            String path = uCloudProvice.upload(inputStream, mimeType, fileName);

            User user = (User) session.getAttribute("user");

            FileTag fileTag = new FileTag();
            fileTag.setSuccess(1);
            //  该URL应该是要显示图片的URL  可以是绝对地址 也可以是相对地址

            fileTag.setUrl(path);

            return fileTag;

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e);
            return null;
        }

    }



    @PostMapping("/updateUserPhoto")
    @ResponseBody
    public Object updateUserPhoto(HttpSession session,HttpServletRequest request){

        Result result = new Result();
        User user = (User) session.getAttribute("user");

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartRequest.getFile("user-photo");

        try {
            InputStream inputStream = file.getInputStream();
            // 通过该文件流 过去该文件最原始的名字和类型
            String fileName = file.getOriginalFilename();
            String mimeType = file.getContentType();

            // 将该文件的流和文件名字和类型 通知给UCloud  并且返回带有效期能访问的URL地址
            String url = uCloudProvice.upload(inputStream,mimeType,fileName);
            Integer count = userService.updateUserAvatorUrlByUserId(user.getId(),url);
            if(count == 1){
                result.setSuccess(true);
            }else{
                result.setSuccess(false);
            }


        } catch (IOException e) {
            e.printStackTrace();

        }


        return result;

    }




}
