package com.example.myboot07.service;

import com.example.myboot07.bean.Comment;
import com.example.myboot07.bean.NotifiCation;
import com.example.myboot07.bean.Quession;
import com.example.myboot07.dto.CommentDTO;

import java.util.List;

public interface ICommentService {
    Integer saveComment(Comment cm, NotifiCation notifiCation);

    List<CommentDTO> queryCommentOnePage(Integer id);

    Integer saveSencondComment(Integer id, String comment, Integer type,Integer userID,NotifiCation notifiCation);

    List<CommentDTO> querySecondComment(Integer id, Integer type);

    List<Quession> queryRelatedCommentByTag(String tag);

    CommentDTO querySecondCommentOneById(Integer parent_id);

    Comment queryCommentOneById(Integer outerid);
}
