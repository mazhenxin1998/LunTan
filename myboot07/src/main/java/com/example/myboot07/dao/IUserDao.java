package com.example.myboot07.dao;

import com.example.myboot07.bean.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;



@Mapper
@Repository
public interface IUserDao {

    /**
     * 通过ID查询User
     */
   User queryUserAll();


    User queryUserById(Integer id);

    void saveUser(@Param("user") User user);

    User queryUserByUserId(Integer id);

    Integer updateUserById(@Param("user") User user);


    User queryUserByToken(@Param("account_Id") Integer account_Id);

    Integer saveUserByRegister(@Param("user") User user);

    User queryUserByAccount(@Param("account") String account);

    User queryUserBycommentator(@Param("commentator") Integer commentator);

    Integer updateUserAvatorUrlByUserId(@Param("id") Integer id,@Param("url") String url);
}
