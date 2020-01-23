package com.example.myboot07.service.impl;

import com.example.myboot07.bean.UserInfo;
import com.example.myboot07.dao.IUserInfoDao;
import com.example.myboot07.service.IUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author lenovo
 */
@Service
public class UserInfoServiceImpl implements IUserInfoService {


    @Autowired
    private IUserInfoDao userInfoDao;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateUserInfoByUserInfo(UserInfo userInfo) {

        Integer count = userInfoDao.updateUserInfoByUserInfo(userInfo);
        if(count != 1){
             //  应该抛出异常
        }

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public UserInfo queryUserInfoByUserId(Integer id) {

        UserInfo userInfo = userInfoDao.queryUserInfoByUserId(id);
        if(userInfo == null){
            //  应该外抛异常
            return null;
        }else{
            return userInfo;
        }

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer insertUserInfo(UserInfo userInfo) {

        System.out.println("测试方法是否执行了");
        Integer count = userInfoDao.insertUserInfoByUserId(userInfo);
        System.out.println("测试增加一条数据之后返回的结果是什么:"+count);
        if(count == 1){
            return count;
        }else{

            return null;
        }

    }
}
