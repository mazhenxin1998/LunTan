package com.example.myboot07.controller;

import com.example.myboot07.bean.*;
import com.example.myboot07.dto.CommentDTO;
import com.example.myboot07.dto.QuestionDTO;
import com.example.myboot07.provide.UCloudProvice;
import com.example.myboot07.service.ICommentService;
import com.example.myboot07.service.INotifiCationService;
import com.example.myboot07.service.IQuestionService;
import com.example.myboot07.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;


/**
 * 负责评论功能块
 */
@Controller
public class CommentController {


    @Autowired
    private ICommentService commentService;

    @Autowired
    private IQuestionService questionService;

    @Autowired
    private INotifiCationService notifiCationService;

    @Autowired
    private IUserService userService;

    @Autowired
    private UCloudProvice uCloudProvice;

    /**
     *       一级评论内容：
     *        每次评论完之后 应该对通知进行更新
     *         发出通知的人： 当前session域中的人
     *         接收通知的人:  被评论问题的的创始人
     *
     *          应该将发布评论的人和接受评论的人 以及被回复的评论或者回复的内容
     *
     *          parent_id :  要被评论问题的id
     *
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/commentUpdate", method = RequestMethod.POST)
    public Object commentUpdate(Integer parent_id, String comment, Integer type,
                                HttpServletRequest request,
                                HttpSession session) {


        Comment cm = new Comment();

        // 将当前时间作为发布评论内容的时间
        String nowTime = new Date().toLocaleString();

        Result result = new Result();

        // 每次进行评论之前应该先对session域中的user进行检查
        User user = (User) session.getAttribute("user");
        if (user == null) {
            result.setCode(2012);
            result.setMessage("想要评论，请先登录哦!");

        } else {
            // 如果当前用户登录了，执行下面的代码
            if (parent_id == null || parent_id == 0) {
                result.setCode(2014);
                result.setMessage("您要操作的评论不存在了,请换一个试试");

            }
            if (comment == null || comment.equals("")) {
                result.setCode(2015);
                result.setMessage("请输入评论内容!");

            }

            // 给cm封装数据并存入数据库
            cm.setGmt_creat(nowTime);
            cm.setComment(comment);
            cm.setParent_id(parent_id);
            // 1 代表是对当前问题的评论  2 代表对评论的评论
            cm.setType(1);

            cm.setCommentator(user.getId());  // 存储在session域中的user对象的id

            NotifiCation notifiCation = getNotification(parent_id,user,1);

            Integer count = commentService.saveComment(cm,notifiCation); //  出错

            if(count == 1){
                result.setCode(200);
                result.setMessage("评论成功!");

            }else{
                // 发表评论失败
                result.setCode(2017);
                result.setMessage("发表评论失败,请稍后在试！");

            }

        }

        return result;
    }

    /**
     *  增加二级评论Integer id,(parent_d)
     *              String comment,
     *              Integer type
     */
    @RequestMapping("/saveSecondComment")
    @ResponseBody
    public Object saveSecondComment(Integer id,
                                    String secondComment,
                                    Integer type,
                                    HttpSession session,
                                    HttpServletRequest request){

        AjaxResult result = new AjaxResult();
        User user0 = (User) request.getSession().getAttribute("user");
        User user = (User) session.getAttribute("user");
        Integer userID = user.getId();
        // 可能是当你要评论的时候这个回复已经被删除
        if(user == null){
            result.setCode(2012);
            result.setLogin(false);
        }else{
            result.setSuccess(true);
            result.setLogin(true);
        }

        NotifiCation notifiCation = getNotification(id,user,2);  //  出错
        Integer count = commentService.saveSencondComment(id,secondComment,type,userID,notifiCation);

        if(count == 1){
            // 增加成功之后 需要将父类的回复数目增加1 在原来的字段上增加1-----integer id
            result.setSuccess(true);
        }else{
            result.setSuccess(false);
        }

        return result;
    }



    /**
     *   查询二级评论列表-----POST请求
     *
     *   通过三个参数来获取一个JSON对象
     *
     *
     */
    @ResponseBody
    @RequestMapping("/secondComment/{id}")
    public List<CommentDTO> secondComment(@PathVariable("id") Integer id) {

        Result result = new Result();
        List<CommentDTO> comments = commentService.querySecondComment(id,2);

        result.setCommentDTOS(comments);
        return comments;

    }

    /**
     *   给出一个comment 和 parent_id  将其封装成一个notification
     *   outerid :  是问题或者评论的id
     */
    public NotifiCation getNotification(Integer parent_id,User user,Integer type){

        NotifiCation notifiCation = new NotifiCation();
        String nowtime = new Date().toLocaleString();
        if(type == 1){
            // 如果type==1  则说明是对问题的评论
            // type == 1  则说明了 outerid为question的id
            notifiCation.setGmt_create(nowtime);
            notifiCation.setNotifier(user.getId());
            QuestionDTO quession = questionService.queryQuestionDtoByQuestionId(parent_id);  //  查出被评论的问题
            notifiCation.setOuterid(parent_id);    //  确定是回复问题   还是回复了评论
            quession.getUser();//  这个是发出通知的人
            notifiCation.setReceiver(quession.getUser().getId());
            notifiCation.setStatus(0); // 0--未读   1--已读
            notifiCation.setType(type);    // 1表示回复问题 2 表示回复评论
        }
        if(type == 2){
            // type == 2 则说明 outerid  为commentid
            notifiCation.setGmt_create(nowtime);
            notifiCation.setNotifier(user.getId());

            // 这里应该根据 id查出唯一的comment(二级)
            CommentDTO commentDTO = commentService.querySecondCommentOneById(parent_id);

            notifiCation.setReceiver(commentDTO.getUser().getId());
            notifiCation.setOuterid(parent_id);    //  确定是回复问题   还是回复了评论
            notifiCation.setStatus(0); // 0--未读   1--已读
            notifiCation.setType(type);    // 1表示回复问题 2 表示回复评论
        }

        return notifiCation;
    }




}
