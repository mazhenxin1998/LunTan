package com.example.myboot07.provide;



import com.alibaba.fastjson.JSON;
import com.example.myboot07.bean.QQBean;
import com.example.myboot07.bean.User;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class QQProvice {

    private String client_id = "101836470";

    private String redirect_uri = "http://127.0.0.1:8080/qqCallBack";

    private String client_secret  = "6b511a2d444603882b330d9398a1e412";

    /**
     *   生成Get请求 利用code 来获取token
     */
    public String getAccessToken(QQBean qqBean){

        OkHttpClient client = new OkHttpClient();
        String token = "";
        String code = qqBean.getCode();
        Request request = new Request.Builder()
//                该URL  可以用token来获取信息    猜测：token应该是随机生成的
                .url("https://graph.qq.com/oauth2.0/token?grant_type=authorization_code&client_id=101836470&client_secret=6b511a2d444603882b330d9398a1e412&code="+code+"redirect_uri=http://127.0.0.1:8080/qqCallBack")
                .build();
        try {
            Response response = client.newCall(request).execute();
            String str = response.body().string();
            // 将页面的响应结果封装成Javabean
            String[] strs = str.split("=");
            System.out.println(strs);
            token = strs[1];
            System.out.println("测试token:"+token);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return token;
    }

    /**
     *   通过token来获取User
     */
    public User getUserByQQ(String token){


        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
//                该URL  可以用token来获取信息    猜测：token应该是随机生成的
                .url("https://graph.qq.com/oauth2.0/me?token=" +token )
                .build();
        try {
            Response response = client.newCall(request).execute();
            String str = response.body().string();
            // 将页面的响应结果封装成Javabean(要确定User里面的属性有有页面上的属性)
            User user = JSON.parseObject(str,User.class);
            return user;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
}


