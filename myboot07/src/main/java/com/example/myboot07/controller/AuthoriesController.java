package com.example.myboot07.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.myboot07.bean.AccessTokenDTO;
import com.example.myboot07.bean.QQBean;
import com.example.myboot07.bean.User;
import com.example.myboot07.provide.GitHubProvice;
import com.example.myboot07.provide.QQProvice;
import com.example.myboot07.service.IUserService;
import com.qq.connect.api.OpenID;
import com.qq.connect.api.qzone.UserInfo;
import com.qq.connect.javabeans.AccessToken;
import com.qq.connect.javabeans.qzone.UserInfoBean;
import com.qq.connect.oauth.Oauth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sun.security.pkcs11.wrapper.Constants;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
public class AuthoriesController {

    /**
     * 声明全局变量
     */
    @Value("${github.client.id}")
    private String client_id;
    @Value("${github.redirect.uri}")
    private String redirect_uri;
    @Value("${github.client.Secret}")
    private String Client_secret;
    @Autowired
    private GitHubProvice gitHubProvice;

    @Autowired
    private IUserService userService;

    @Autowired
    private QQProvice qqProvice;


    /**
     * 该接口是用户第一次第一次登陆获取code
     * <p>
     * 注意细节：
     * 在controller中String方法回一个String的字符串，实际上不是跳转到指定的页面，而是将当前页面渲染为指定页面的内容
     * <p>
     * <p>
     * 登录controller
     * <p>
     * <p>
     * 每次登陆前应该向数据库中增加登陆用户的信息，如果该用户已经存在，则更新该用户的信息 ，比如说用户换了个头像或者名字(未完成)
     */

    @GetMapping("/callback")
    public String callBack(@RequestParam(value = "code") String code,
                           @RequestParam("state") String state,
                           HttpSession session,
                           HttpServletResponse response) throws IOException {

        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();

        accessTokenDTO.setClient_id(client_id);

        accessTokenDTO.setClient_secret(Client_secret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirect_uri);
        accessTokenDTO.setState(state);
        // 执行POST请求， 调用GitHub  access_token接口 获取access_token 数据
        String accessToken = gitHubProvice.getAccessToken(accessTokenDTO);

        // 执行Get请求， 调用GitHub  User 接口， 来获取当前登录的GitHubUser对象
        // 前台传过来的User的ID为数据库中的accountid
        User user = gitHubProvice.getGitHubUser(accessToken);

        user.setToken(accessToken);
        user.setAccount_id(user.getId());

        // 返回1说明保存成功， 如果返回0说明该数据库中已经有了该用户  保存用户失败 灭有做处理
        Integer number = userService.saveUser(user);

        Integer id = user.getAccount_id();

        User u = userService.queryUserByToken(id);

        // 注意： session域中的user的id是错的 需要根据tokneid查出自增的id
        session.setAttribute("user", u);

        response.addCookie(new Cookie("userid", user.getId().toString()));

        if (number == 1) {
            System.out.println(user.getName() + "登录成功");
        }
        return "redirect:/";
    }

    /**
     *  QQ 回调地址
     */
    @GetMapping("/qqCallBack")
    public String qqCallBack(@RequestParam(value = "code")String code,
                             @RequestParam(value = "state") String state,
                             HttpSession session,
                             HttpServletResponse response){
        System.out.println("测试方法是否执行力 ");
        String client_id = "101832639";
        String redirect_uri = "http://127.0.0.1:8080/qqCallBack";
        String client_secret = "6b511a2d444603882b330d9398a1e412";
        QQBean qqBean = new QQBean();
        qqBean.setClient_id(client_id);
        qqBean.setRedirect_uri(redirect_uri);
        qqBean.setCode(code);
        qqBean.setState(state);
        qqBean.setClient_secret(client_secret);

        //  获取 token
        String accesstoken = qqProvice.getAccessToken(qqBean);
        System.out.println("测试获取到的"+accesstoken);


        return "";
    }


    /**
     *  QQLogin
     */
    @RequestMapping("/qqAuth")
    public String qqAuth(HttpServletRequest request) {
        try {
            String authorizeURL = new Oauth().getAuthorizeURL(request);
            return "redirect:" + authorizeURL;
        } catch (Exception e) {
            return null;
        }
    }

//    @RequestMapping("/qqCallBack")
//    public String qqLoginBack(HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
//
//        return "";
//    }

}
