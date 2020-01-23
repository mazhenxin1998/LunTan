package com.example.myboot07.service.impl;

import com.example.myboot07.bean.User;
import com.example.myboot07.dao.IUserDao;
import com.example.myboot07.service.IUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService implements IUserService {

    /**
     *  声明全局变量
     */
    @Autowired
    private IUserDao userDao;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void sayHello() {
        User user = userDao.queryUserAll();
    }

    /**
     * 将每次登陆的用户信息存储在数据库中   但是每次存储前应该访问数据库当前用户是否存在
     * @param user
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer saveUser(User user) {

         // 该语句执行完  那么数据库对应的user应该已经有了了正确的值
         User u = userDao.queryUserById(user.getAccount_id());

        if(u == null){
           // 如果user为空则保存
            userDao.saveUser(user);
            return 1;
        }else {
            // 如果user不为空  那么应该更新下数据库中数据
            try {
                Integer number = userDao.updateUserById(user);
            }catch (Exception e){
                System.out.println("更新用户失败了!");
            }
            return 0;
        }




    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer updateUserAvatorUrlByUserId(Integer id, String url) {

        Integer count = userDao.updateUserAvatorUrlByUserId(id,url);
        if(count == 0){
            // 应该抛出异常
            return null;
        }else{
            return count;
        }

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public User queryUserByUserId(Integer id) {

        User user = userDao.queryUserByUserId(id);

        if(user == null){
            // 应该抛异常
            return null;
        }else{
            return user;
        }

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public User queryUserByAccount(String account) {

        // 在service层中抛出异常

        User user = userDao.queryUserByAccount(account);

        if(user != null){
            return user;
        }else{
            // 这里应该抛出一个异常---目前暂时不管
            return user;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer saveUserByRegister(User user) {
        // 在service层抛异常
        Integer count = userDao.saveUserByRegister(user);

        if(count == 1){
            return count;
        }else{
            // 这里应该准确的说 是抛出异常
            return -1;
        }

    }

    /**
     *  根据token查出一个user
     * @param account_Id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public User queryUserByToken(Integer account_Id) {

        User user = userDao.queryUserByToken(account_Id);

        if(user != null){
            return user;
        }else{
            //  如果user为空 那么向上抛出异常
            throw new RuntimeException("");
        }


    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public User queryUserById(Integer id) {
        return userDao.queryUserById(id);
    }
}
