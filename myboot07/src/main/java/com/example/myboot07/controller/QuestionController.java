package com.example.myboot07.controller;

import com.example.myboot07.bean.*;
import com.example.myboot07.dao.IQussionDao;
import com.example.myboot07.dao.IUserDao;
import com.example.myboot07.dto.NotifiCationDTO;
import com.example.myboot07.dto.QuestionDTO;
import com.example.myboot07.service.ICommentService;
import com.example.myboot07.service.INotifiCationService;
import com.example.myboot07.service.IQuestionService;
import com.example.myboot07.service.IUserService;
import jdk.jfr.events.ThrowablesEvent;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@Controller
public class QuestionController {

    /**
     * 全局变量
     */

    @Autowired
    private IQuestionService questionService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IQussionDao qussionDao;

    @Autowired
    private IUserDao userDao;

    @Autowired
    private INotifiCationService notifiCationService;

    @Autowired
    private ICommentService commentService;

    ThreadLocal<Integer> unReplySize = new ThreadLocal<Integer>() {

        @Override
        protected Integer initialValue() {
            return 0;
        }

        @Override
        public Integer get() {
            return super.get();
        }

        @Override
        public void set(Integer value) {
            super.set(value);
        }
    };

    /**
     * 默认 ：
     * POST  渲染页面也处理请求
     * Get   处理请求
     * <p>
     * <p>
     * 表单回显思路：当有空的时候提交不成功，既表单回显
     * 1。将表单页面所有的数据放到request域中
     * 2. 当有一个表单数据为空，那么就将所有数据回显
     * 3. 但同时也需要判断session域中是否存有User的相关信息
     * a. 如果存有，可以直接发布问题
     * b. 如果没有存有，那么就请先登录
     * <p>
     * 最后用监听器处理权限问题
     * 在这里的权限问题为： 如果用户没有登录，那么就请先登录，在发布问题
     *
     *           发布问题(当前用户)
     *
     * @return
     */
    @ResponseBody
    @PostMapping("/doquestion")
    public Object doQuestion(@RequestParam(value = "title", required = false) String title,
                             @RequestParam(value = "description", required = false) String description,
                             @RequestParam(value = "tag", required = false) String tag,
                             HttpServletRequest request) {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        Result result = new Result();

        // 将数据进行封装
        Quession quession = getQuession(title,description,tag,user.getId());

        //  如果两者都不为空且向数据库中插入数据正常 那么就将session域中的result对象设置true
        Integer count = questionService.saveQuestion(quession);
        if (count == 1) {
            result.setSuccess(true);
            // 如果等于1 说明保存成功
        } else {
            // 如果不等于1 说明前台传过来的title和description  不为空，但是保存出错
            // 保存失败则说明出现了异常
            result.setCode(3001);
            result.setSuccess(false);
        }


        return result;
    }

//    对我的问题页面的问题的标题进行条状

    @RequestMapping("/doquestion/{id}")
    public String modiFileQuestion(@PathVariable("id") Integer id,
                                   Model model) {
        QuestionDTO questionDTO = questionService.queryQuestionDtoByQuestionId(id);
        model.addAttribute("questionid", id);
        model.addAttribute("question", questionDTO);

        return "modiFile";
    }


    /**
     * 处理我的问题页面
     */
    @RequestMapping("/dealWithMyQuestion")
    public String dealWith(@RequestParam(value = "profileName", defaultValue = "我的问题") String profileName,
                           @RequestParam(value = "pageno", required = false, defaultValue = "1") Integer pageno,
                           @RequestParam(value = "pagesize", required = false, defaultValue = "7") Integer pagesize,
                           HttpSession session,
                           Model model) {

        //  对我的问题处理 思路： 利用session域中的user对象中的id进行查询 ----通过creater
        User user = (User) session.getAttribute("user");
        Integer num = 0;
        if ("myquestion".equals(profileName)) {
            model.addAttribute("mytitle", "我的提问");
        }
        // 查出的集合为未读的集合
        List<NotifiCation> notifiCations = notifiCationService.queryNotificationByReceiver(user.getId());
        // 应该判断该notifications 是不是为空或者长度为0 如果长度为0或者为空,应该抛出异常让程序正常执行
        if (notifiCations == null || notifiCations.size() == 0) {
            //   应该抛异常
        }
        // upReplySize   应该设置为status为0时候的总数量
        if (notifiCations != null) {

            for (NotifiCation notifiCation : notifiCations) {
                if (notifiCation.getStatus() == 0) {
                    num = num + 1;
                }
            }
            System.out.println("测试number的值:" + num);
            unReplySize.set(num);
        }else {
            unReplySize.set(0);
        }
        //   处理最新回复
        if ("myreply".equals(profileName)) {
            model.addAttribute("mytitle", "最新回复");
            //  确定该部分为跳转到最新回复(老步骤)  该集合的size为当前用户未读的问题或者回复的数量

            List<NotifiCationDTO> notifiCationDTOS = new LinkedList<NotifiCationDTO>();
            //  根据notification的信息确定出一个notificationdto  notifiCations.size  有可能为0     ----notifiCationDTOS.size() == 0 ||
            if( notifiCations.size() == 0){
                System.out.println("QuestionController出现Null--------为空判断");
            }else{
                Iterator itea = notifiCations.iterator();
                while (itea.hasNext()) {
                    NotifiCation notifiCation = (NotifiCation) itea.next();
                    NotifiCationDTO notifiCationDTO = new NotifiCationDTO();
                    // 根据notifier 和 reciver 来查出两个user对象
                    User notifiUser = userService.queryUserByUserId(notifiCation.getNotifier());
                    User receiverUser = userService.queryUserByUserId(notifiCation.getReceiver());
                    // 再根据type和outerid查出question或者comment   总之 一个notificationdto确定一个question 或者 comment  但是两者不能同时拥有
                    if (notifiCation.getType() == 1) {
                        // 查找问题
                        QuestionDTO questionDTO = questionService.queryQuestionDtoByQuestionId(notifiCation.getOuterid());
                        notifiCationDTO.setQuestionDTO(questionDTO);
                    }
                    if (notifiCation.getType() == 2) {
                        //  查找comment评论
                        Comment comment = commentService.queryCommentOneById(notifiCation.getOuterid());
                        notifiCationDTO.setComment(comment);
                    }
                    notifiCationDTO.setNotifierUser(notifiUser);
                    notifiCationDTO.setReceiverUser(receiverUser);
                    notifiCationDTO.setStatus(notifiCation.getStatus());

                    notifiCationDTO.setType(notifiCation.getType());
                    // 将每一个dto放入到dto集合中
                    notifiCationDTOS.add(notifiCationDTO);

                }
                model.addAttribute("notifiCationDTOS", notifiCationDTOS);
            }
//            Iterator itea = notifiCations.iterator();
//            while (itea.hasNext()) {
//                NotifiCation notifiCation = (NotifiCation) itea.next();
//                NotifiCationDTO notifiCationDTO = new NotifiCationDTO();
//                // 根据notifier 和 reciver 来查出两个user对象
//                User notifiUser = userService.queryUserByUserId(notifiCation.getNotifier());
//                User receiverUser = userService.queryUserByUserId(notifiCation.getReceiver());
//                // 再根据type和outerid查出question或者comment   总之 一个notificationdto确定一个question 或者 comment  但是两者不能同时拥有
//                if (notifiCation.getType() == 1) {
//                    // 查找问题
//                    QuestionDTO questionDTO = questionService.queryQuestionDtoByQuestionId(notifiCation.getOuterid());
//                    notifiCationDTO.setQuestionDTO(questionDTO);
//                }
//                if (notifiCation.getType() == 2) {
//                    //  查找comment评论
//                    Comment comment = commentService.queryCommentOneById(notifiCation.getOuterid());
//                    notifiCationDTO.setComment(comment);
//                }
//                notifiCationDTO.setNotifierUser(notifiUser);
//                notifiCationDTO.setReceiverUser(receiverUser);
//                notifiCationDTO.setStatus(notifiCation.getStatus());
//
//                notifiCationDTO.setType(notifiCation.getType());
//                // 将每一个dto放入到dto集合中
//                notifiCationDTOS.add(notifiCationDTO);
//
//            }
            //  当前页面上显示的最新回复
            // 1. 该集合含有 :  发出通知的user 和接受通知的user
            // 2. 被回复的评论或者回复
            // 3. 该用户所有通知的状态
            // 4. 。。。

//            model.addAttribute("notifiCationDTOS", notifiCationDTOS);

        }

        model.addAttribute("unReplySize", unReplySize);

        //  对数据 进行处理
        // 前提必须知道当前用户拥有几天问题
        List<Quession> quessionList = questionService.queryQuestionAll();

        // number 数量为总的页码数
        int number = quessionList.size() % 7 == 0 ? quessionList.size() / 7 : quessionList.size() / 7 + 1;

        if (pageno < 1) {
            pageno = 1;
        }

        if (pageno > number) {
            pageno = number;
        }

        // 给page封装属性
        Page page = new Page(pageno, pagesize);

        //  设置总的问题 那么当前页码也会算出来
        page.setTotalsize(quessionList.size());

        List<Quession> quessions = questionService.queryQuestionDtoAllByCreator(pageno, pagesize, user.getId());  // 分页查询

        List<QuestionDTO> questionDTOS = new LinkedList<QuestionDTO>();

        for (Quession quession : quessions) {
            QuestionDTO dto = new QuestionDTO();
            BeanUtils.copyProperties(quession, dto);
            dto.setUser(user);
            questionDTOS.add(dto);
        }

        page.setList(questionDTOS);
        // 向request域中加入   我的提问数量  应该查出该用户所拥有的所有问题
        Integer myQuestionCount = questionService.queryMyQuestionSize(user.getId());
        // myquestionSize  是我的提问问题的总数量
        model.addAttribute("myQuestionSize", myQuestionCount);

        model.addAttribute("pages", page);

        return "profile";
    }

    /**
     * 删除Question
     */
    @ResponseBody
    @RequestMapping("/deleteQuestion")
    public Object deleteQuestion(Integer id, HttpSession session) {

        User user = (User) session.getAttribute("user");
        Result result = new Result();

        Integer count = questionService.deleteQuestionById(id);

        if (count == 1) {
            result.setSuccess(true);
        } else {
            result.setSuccess(false);
        }

        return result;
    }

    /**
     * 对当前页面上的问题进行点赞处理
     */
    @ResponseBody
    @RequestMapping("/updateViewCount")
    public Object updateViewCount(Integer id) {

        Result result = new Result();
        Integer count = questionService.updateQuestionLikeCount(id);

        if (count == 1) {
            // 增加成功
            result.setSuccess(true);
        } else {
            // 增加失败
            result.setSuccess(false);
        }

        return result;
    }


    public Quession getQuession(String title,String description,String tag,Integer creator){

        Quession quession = new Quession();
        quession.setTitle(title);
        quession.setTag(tag);
        quession.setDescription(description);
        quession.setCreator(creator);
        //  设置问题创建时间  应该将日期(day)  也应该弄为两位数
        String nowDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        StringBuffer sb = new StringBuffer();
        String[] nowDates = nowDate.split(" ");

        String[] dates = nowDates[0].split("-");
        for(int i=0 ;i<1;i++){
            sb.append(dates[i]);
            sb.append("-");

        }
        //  判断月份是否规范
        if(dates[dates.length-2].length() == 1){
            String s = "0"+dates[dates.length-2];
            sb.append(s);
        }else{
            sb.append(dates[dates.length-2]);
        }
        sb.append("-");
        //  判断天数是否规范
        if(dates[dates.length -1].length() == 1){
            String s = "0"+dates[dates.length-1];
            sb.append(s);
        }else{
            sb.append(dates[dates.length-1]);
        }
        sb.append(" ");
        sb.append(nowDates[1]);
        quession.setGmt_creat(sb.toString());
        System.out.println(sb.toString());
        return quession;

    }

}
