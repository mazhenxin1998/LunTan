package com.example.myboot07.service.impl;

import com.example.myboot07.bean.UserRegister;
import com.example.myboot07.dao.ILoginDao;
import com.example.myboot07.service.ILoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LoginServiceImpl implements ILoginService {

    /**
     *  声明全局变量
     */
    @Autowired
    private ILoginDao loginDao;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer saveUserRegister(UserRegister userRegister) {

       Integer count = loginDao.saceUserRegister(userRegister);

        if(count == 1){
            // 如果保存成功那么就返回
            return count;
        }else{
            // 如果保存失败那么就返回-1
            return -1;
        }
    }
}
