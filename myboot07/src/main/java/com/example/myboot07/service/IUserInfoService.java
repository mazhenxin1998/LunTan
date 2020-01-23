package com.example.myboot07.service;

import com.example.myboot07.bean.UserInfo;

/**
 * @author lenovo
 */
public interface IUserInfoService {

    Integer insertUserInfo(UserInfo userInfo);

    UserInfo queryUserInfoByUserId(Integer id);

    void updateUserInfoByUserInfo(UserInfo userInfo);
}
