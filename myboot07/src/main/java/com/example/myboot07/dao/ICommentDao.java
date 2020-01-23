package com.example.myboot07.dao;

import com.example.myboot07.bean.Comment;
import com.example.myboot07.bean.Quession;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ICommentDao {

    Integer saveComment(@Param("cm") Comment cm);

    List<Comment> queryCommentOnePage(@Param("id") Integer id);

    Integer saveSecondComment(@Param("cm") Comment cm);

    List<Comment> querySecondCommentOnePage(@Param("id") Integer id, @Param("type") Integer type);

    void updateCommentReply(Integer id);

    List<Quession> queryRelatedCommentByTag(@Param("tag") String tag);


    Comment querySecondCommentOneById(@Param("parent_id") Integer parent_id);

    Comment queryCommentOneById(@Param("outerid") Integer outerid);
}
