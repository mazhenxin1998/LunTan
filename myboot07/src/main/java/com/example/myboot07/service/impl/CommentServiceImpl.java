package com.example.myboot07.service.impl;

import com.example.myboot07.bean.Comment;
import com.example.myboot07.bean.NotifiCation;
import com.example.myboot07.bean.Quession;
import com.example.myboot07.bean.User;
import com.example.myboot07.dao.ICommentDao;
import com.example.myboot07.dao.IQussionDao;
import com.example.myboot07.dao.IUserDao;
import com.example.myboot07.dto.CommentDTO;
import com.example.myboot07.service.ICommentService;
import com.example.myboot07.service.INotifiCationService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
public class CommentServiceImpl implements ICommentService {

    @Autowired
    private ICommentDao commentDao;

    @Autowired
    private IQussionDao qussionDao;

    @Autowired
    private IUserDao userDao;

    @Autowired
    private INotifiCationService notifiCationService;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public Comment queryCommentOneById(Integer outerid) {

        Comment comment = commentDao.queryCommentOneById(outerid);
        if(comment == null){
            return null;
        }else{
            return comment;
        }

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CommentDTO querySecondCommentOneById(Integer parent_id) {

        // 1. 应该先根据id查出一个comment
        Comment comment = commentDao.querySecondCommentOneById(parent_id);
        CommentDTO commentDTO = new CommentDTO();
        if(comment == null){
            //  抛异常
        }else{

            commentDTO.setComment(comment.getComment());
            commentDTO.setCommentator(comment.getCommentator());
            commentDTO.setGmt_creat(comment.getGmt_creat());
            commentDTO.setParent_id(comment.getParent_id());
            commentDTO.setReply(comment.getReply());
            commentDTO.setType(comment.getType());
            User user = userDao.queryUserByUserId(comment.getCommentator());
            commentDTO.setUser(user);

        }


        return commentDTO;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<Quession> queryRelatedCommentByTag(String tag) {

        List<Quession> comments = commentDao.queryRelatedCommentByTag(tag);
        if(comments == null){
            // 应该抛出异常
            return null;
        }else{
            return comments;
        }

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<CommentDTO> querySecondComment(Integer id, Integer type) {

        // 应该先根据id 和 type查出匹配的二级评论
        List<Comment> list = commentDao.querySecondCommentOnePage(id,type);
        List<CommentDTO> commentDTOS = new LinkedList<CommentDTO>();
        for (Comment comment : list) {
            CommentDTO commentDTO = new CommentDTO();
            User user = userDao.queryUserByUserId(comment.getCommentator());
            BeanUtils.copyProperties(comment,commentDTO);
            commentDTO.setUser(user);

            commentDTOS.add(commentDTO);
        }



        if(list.size() == 0){
            // 说明查找失败,抛出异常
            return null;
        }else{
            return commentDTOS;
        }

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer saveSencondComment(Integer id, String comment, Integer type,Integer userid,NotifiCation notifiCation) {

        Comment cm = new Comment();
        cm.setParent_id(id);
        cm.setType(type);
        cm.setComment(comment);
        String nowTime = new Date().toLocaleString();
        cm.setGmt_creat(nowTime);
        cm.setCommentator(userid);
        Integer count = commentDao.saveSecondComment(cm);

        if(count == 1){

            //  对当前ID 的回复数  当前字段+1
            commentDao.updateCommentReply(id);
            // 应该将id放入到outerid
            notifiCationService.insertNotification(notifiCation);
            return count;
        }else{
            // 应该抛出异常
            System.out.println("在保存二级回复内容时候出现错误CommentService");
            return null;
        }

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<CommentDTO> queryCommentOnePage(Integer id) {
        // id是被评论的问题的id   type默认是1
        List<Comment> comments = commentDao.queryCommentOnePage(id);  // 该集合是一个问题的所有回复 的一页数据  ---错的

        List<CommentDTO> lists = new LinkedList<CommentDTO>();
        // 为每一个comment设置创造者
        for (Comment comment : comments) {

            CommentDTO commentDTO = new CommentDTO();
            User user = userDao.queryUserBycommentator(comment.getCommentator());

            // 为每一个commentdto设置属性
            commentDTO.setUser(user);
            commentDTO.setComment(comment.getComment());
            commentDTO.setGmt_creat(comment.getGmt_creat());
            commentDTO.setType(comment.getType());
            commentDTO.setParent_id(comment.getParent_id());
            commentDTO.setId(comment.getId());
            commentDTO.setReply(comment.getReply());

            // 将每一个commentdto放入到lists集合
            lists.add(commentDTO);
        }

        if (comments == null) {
            // == null 说明出错 抛出异常
            return null;
        } else {
            return lists;
        }

    }

    /**
     * 评论成功的时候需要将当前问题的comment_count数目+1
     *
     * @param cm
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer saveComment(Comment cm, NotifiCation notifiCation) {
        // 异常在service层抛出
        System.out.println(notifiCation);
        System.out.println(cm);
        Integer count = commentDao.saveComment(cm);
        System.out.println("测试comment主键回填id"+cm.getId());
        //  更新评论数
        Integer number = qussionDao.updateQuestionCommentCountById(cm.getParent_id());
        if (count == 1 && number == 1) {
            // 评论成功 之后再这里加上对notification的操作
            notifiCationService.insertNotification(notifiCation);
            return 1;
        } else {
            //  向外抛出异常
          return null;
        }

    }



}
