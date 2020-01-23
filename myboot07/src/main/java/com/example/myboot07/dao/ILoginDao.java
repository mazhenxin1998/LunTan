package com.example.myboot07.dao;

import com.example.myboot07.bean.UserRegister;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ILoginDao {

    Integer saceUserRegister(@Param("userRegister") UserRegister userRegister);
}
