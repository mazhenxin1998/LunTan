package com.example.myboot07.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller()
@RequestMapping("/error")
public class CustomsizeErrorController implements ErrorController {


    /**
     *
     *  作用： getErrorPath 方法返回的字符串  则对未处理过的异常进行处理，只要是未处理过的异常发生，那么该方法
     *         就会执行
     *  该方法会覆盖默认的getErrorPath方法也就是可以自己自定义错误页面
     *
     * @return  返回的字符串，则去Spring默认的视图解析器下寻找默认的错误页面
     */
    @Override
    public String getErrorPath() {
        // 自定义的错误页面路径
        return "error";
    }

    @RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView handle (HttpServletRequest request,
                                Model model){

        // 需要通过request获取状态码而不是直接在参数里获取
        HttpStatus status = getStatus(request);

        // 通过request获取状态码 ---- 通过该状态码可以对一些模板类的status进行处理
        if(status.is5xxServerError()){
            // 5xx 错误是服务器内部的错误  将错误信息message和状态码status也放入到request域中
            model.addAttribute("message", "服务器内部出故障了，请稍后访问!");
            model.addAttribute("code",status);
        }

        if(status.is4xxClientError()){
            // 4xx 是客户端请求出错
            model.addAttribute("message","您的请求有毛病,请稍后在试!");
            model.addAttribute("code",status);
        }

        return new ModelAndView("loginError");
    }

    /**
     *   获取状态码
     */

    private HttpStatus getStatus(HttpServletRequest request) {
        // statusCode 是单独的状态码
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        // 由状态码去获取该状态以及错误信息
        return HttpStatus.valueOf(statusCode);
    }

}
