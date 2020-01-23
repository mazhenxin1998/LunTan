package com.example.myboot07.intercepter;

import com.example.myboot07.bean.Quession;
import com.example.myboot07.bean.User;
import com.example.myboot07.service.IQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 自定义拦截器
 *  HandlerInterceptor :  拦截器
 *      实现拦截器 那么当前类就是一个拦截器类
 *  实现之后 需要将该拦截器放入到IOC交给Spring来管理
 */
@Component
public class MyInterceptor implements HandlerInterceptor {

    /**
     *   声明全局变量
     */
    @Autowired
    private IQuestionService  questionService;

    /**
     * 在请求路径中允许访问的情况下  对该次请求作出处理
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        User user = (User) request.getSession().getAttribute("user");

        // 将当前user所发布的所有问题的ID查出来 并放入到黑名单 也应该将当前user所发布的所有评论也加入
        List<Quession> quessions = questionService.queryQuestionAll();

        // 该集合中只存放无权限路径
        List<String> urls = new ArrayList<String>();

        // 发布页面
        urls.add("/publish");
        // 我的问题页面
        urls.add("/domyquestion");

        //  将更新我的问题路径加入黑名单
        urls.add("/toupdate");
        urls.add("/update");
        urls.add("/doquestion");
        //urls.add("/commentUpdate");
        urls.add("/saveSecondComment");
        urls.add("/update");
        urls.add("/publish");
        urls.add("/doquestion");
        urls.add("/dealWithMyQuestion");
        

        urls.add("/logout");

        for(Quession quession : quessions){
            urls.add("/publish"+quession.getId());
        }

        // 获取当前请求路径(也就是一个RequestMapping/GetMapping/POSTMapping)
        String path = request.getServletPath();
        // 获取上下文
        String contextPath = request.getContextPath();

        // 直接判断user不空的情况下直接放行
        if(user !=null){
            return true;
        }

        if (user == null) {
            if (urls.contains(path)) {
                // 请求转发到根路径下 也就是首页 重定向：2次
                // 具体路径
                response.sendRedirect(contextPath + "/");
                return false;
            }

        }


        return true;
    }
}
