package com.example.myboot07.service;

import com.example.myboot07.bean.User;

public interface IUserService {

    void sayHello();

    Integer saveUser(User user);

    User queryUserById(Integer id);

    User queryUserByToken(Integer token);

    Integer saveUserByRegister(User user);

    User queryUserByAccount(String account);

    User queryUserByUserId(Integer id);

    Integer updateUserAvatorUrlByUserId(Integer id, String path);
}
