package com.example.myboot07.dao;

import com.example.myboot07.bean.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ITagDao {
    List<Tag> queryTagAll();
}
