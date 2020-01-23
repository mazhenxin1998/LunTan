package com.example.myboot07.dao;

import com.example.myboot07.bean.Text;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface TextDao {

    Integer insertTest(Text text);


}
