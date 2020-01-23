package com.example.myboot07.dao;

import com.example.myboot07.bean.Quession;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface IQussionDao {

    /**
     *  保存问题
     *      1. title
     *      2. description
     *      3. crator
     * @param quession
     * @return
     */
    Integer saveQuestion(@Param("quession") Quession quession);

    List<Quession> queryQuestionAll();

    /**
     *  查询出每一页上面的数据 也就是默认前8条数据
     * @param pagesize
     * @return
     */
    List<Quession> queryQuestionPage(@Param("pageno") Integer  pageno,@Param("pagesize") Integer pagesize);

    Quession queryQuestionByID(@Param("id") Integer id);

    Integer updateQuestion(@Param("quession") Quession quession);

    List<Quession> queryQuestionDtoAllByCreator(@Param("starIndex") Integer starIndex, @Param("pagesize") Integer pagesize, @Param("id") Integer id);

    Integer updateQuestionViewCount(Integer id);

    Integer updateQuestionCommentCountById(@Param("parent_id") Integer parent_id);

    void updateQuestionTag(String[] tags);

    Integer queryMyQuestionSize(@Param("id") Integer id);

    Integer deleteQuestionById(@Param("id") Integer id);

    Integer updateQuestionLikeCount(@Param("id") Integer id);

    List<Quession> queryQuestionBySearchFunction(@Param("stardIndex") Integer stardIndex,@Param("pagesize") Integer pagesize,@Param("search") String search);

    List<Quession> queryQuestionAllBySearch(@Param("search") String search);
}
