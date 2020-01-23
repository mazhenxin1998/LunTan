package com.example.myboot07.dao;

import com.example.myboot07.bean.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author lenovo
 */
@Repository
@Mapper
public interface IUserInfoDao {

    Integer insertUserInfoByUserId(@Param("userInfo") UserInfo userInfo);

    UserInfo queryUserInfoByUserId(@Param("userid") Integer userid);

    Integer updateUserInfoByUserInfo(@Param("userInfo") UserInfo userInfo);
}
