package com.example.myboot07.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Ejb3TransactionAnnotationParser;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义异常处理处理
 * <p>
 * <p>
 * 细节:  不能同时返回ModelAndView或者json格式
 * 处理： 可以通过response的形式 直接返回json格式的字符串
 * 既通过response获取writer来直接写入到页面上
 *
 *
 *
 *       枚举常量不会用
 *
 *
 *
 */
//@ControllerAdvice   //  不加参数则默认将所有的controller中出现的异常都抛到这里
//public class CustomsizeException {
//
//
//
//    /**
//     *   处理404()
//     * @param request
//     * @param e
//     * @param model
//     * @return
//     */
//    @ExceptionHandler(Exception.class)
//    ModelAndView handle(HttpServletRequest request, Throwable e, Model model) {
//
//        HttpStatus status = getStatus(request);
//        System.out.println(status);
//        if(status.is5xxServerError()){
//            model.addAttribute("message","请登录之后再来进行评论");
//            return new ModelAndView("loginError");
//        }
//        System.out.println("1");
//        return new ModelAndView("404");
//    }
//
//    /**
//     *    处理自定义异常
//     * @param request
//     * @return
//     */
//
//    @ExceptionHandler(ExceptionMe.class)
//    ModelAndView handleMe(HttpServletRequest request, HttpServletResponse response, Throwable e, Model model){
//
//        System.out.println("测试自定义异常是否能执行");
//        // e ： 自定义异常
//
//        HttpStatus status = getStatus(request);
//        if("您要查找的问题不存在,请换一个试试".equals(e.getMessage())){
//            model.addAttribute("message","您要查找的问题不存在,请换一个试试");
//            return new ModelAndView("404");
//        }
//        if("请先登录,登录之后查看您的个人信息".equals(e.getMessage())){
//            // 应该添加提示信息
//            return new ModelAndView("loginError");
//        }
//        if("请登录之后再来进行评论".equals(e.getMessage())){
//            // 应该添加提示信息
//            try {
//                request.getRequestDispatcher("loginError").forward(request,response);
//            } catch (ServletException ex) {
//                ex.printStackTrace();
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
//
//            return new ModelAndView("loginError");
//        }
//
//        // 处理500异常
//        if(status.is5xxServerError()){
//            return new ModelAndView("500");
//        }
//        System.out.println("3");
//        // 默认返回服务器异常
//        return new  ModelAndView("500");
//
//    }
//
//    private HttpStatus getStatus(HttpServletRequest request) {
//        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
//        if (statusCode == null) {
//            return HttpStatus.INTERNAL_SERVER_ERROR;
//        }
//        return HttpStatus.valueOf(statusCode);
//    }
//
//}
