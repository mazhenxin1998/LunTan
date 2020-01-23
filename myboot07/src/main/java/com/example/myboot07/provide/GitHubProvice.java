package com.example.myboot07.provide;

import com.alibaba.fastjson.JSON;
import com.example.myboot07.bean.AccessTokenDTO;
import com.example.myboot07.bean.User;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 *   用OKhttp来发送get和post请求，来获取数据
 */
@Component   // 向IOC容器中注册为一个bean
public class GitHubProvice {

    /**
     *  生成POST请求，用code来交换 access——token
     */
    public String getAccessToken(AccessTokenDTO accessTokenDTO) {

        MediaType mediaType = MediaType.get("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String s = response.body().string();
            String strings = s.split("&")[0];
            //  截取token的值，以便传入下面的方法来获取GitHub的User对象，也就是当前登录的对象
            String[] token = strings.split("=");
            return token[1];

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *  生成GET请求 用GetAccessToken方法获取的access——token  来交换User的信息
     */
    public User getGitHubUser(String accessToken) {
        // 生成OKHTTP对象
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
//                该URL  可以用token来获取信息    猜测：token应该是随机生成的
                .url("https://api.github.com/user?access_token=" + accessToken)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String str = response.body().string();
            // 将页面的响应结果封装成Javabean
            User user = JSON.parseObject(str,User.class);
            return user;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



}
