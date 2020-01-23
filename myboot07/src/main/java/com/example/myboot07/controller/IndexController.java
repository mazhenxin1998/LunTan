package com.example.myboot07.controller;


import com.example.myboot07.bean.*;
import com.example.myboot07.dto.NotifiCationDTO;
import com.example.myboot07.dto.QuestionDTO;
import com.example.myboot07.service.ICommentService;
import com.example.myboot07.service.INotifiCationService;
import com.example.myboot07.service.IQuestionService;
import com.example.myboot07.service.IUserService;
import com.example.myboot07.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.security.PublicKey;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@Controller
public class IndexController {

    /**
     * 声明全局变量
     */
    @Autowired
    private IQuestionService questionService;

    @Autowired
    private INotifiCationService notifiCationService;

    @Autowired
    private ICommentService commentService;

    @Autowired
    private IUserService userService;

    ThreadLocal<Integer> unreadSize = new ThreadLocal<Integer>(){
        //  初始化线程安全变量
        @Override
        protected Integer initialValue() {
            return 0;
        }
        // 声明get和set方法
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
     * 进行跳转向主页面  并进行一定的业务逻辑处理
     * 思路 ：
     * 1. 调用service  放回一个QuestionDTO对象
     * 2. 由于要复制对象，Spring提供了一个简单的工具类，来复制方法
     *
     *
     *       通知:  1. 先确定当前是否为登录状态 如果是登录状态则进行相关操作
     *              2. 根据当前session域中的user进行查询出当接受者的id为当前userid并且只查询未读的 既status为0的notification
     *              3. 查询出来之后应该根据type在进行第二次分类
     *              4. 根据分类之后的结果在页面上进行显示
     *
     *
     */
    @GetMapping({"/","/index.html"})
    public String  toIndex(@RequestParam(value = "pageno",required = false ,defaultValue = "1") Integer pageno,
                          @RequestParam(value = "pagesize",required = false,defaultValue = "7") Integer pagesize,
                          HttpServletRequest request,
                          HttpServletResponse response,
                          Model model) {

        Integer num = 0;

        // 根据当前user的id查出所有recver
        User user = (User) request.getSession().getAttribute("user");
        List<NotifiCationDTO> notificationQuestion = new LinkedList<NotifiCationDTO>();
        List<NotifiCationDTO> notificationComment = new LinkedList<NotifiCationDTO>();
        List<NotifiCation> notifiCations = null;

        if(user !=null){
            //  肯定是空
            notifiCations = notifiCationService.queryNotificationByReceiver(user.getId());
            if(notifiCations != null){

                // unreadSize 的值是应该是status为0的总的数量
                for (NotifiCation notifiCation : notifiCations) {
                    if(notifiCation.getStatus() == 0){
                        num = num + 1;
                    }
                    unreadSize.set(num);
                }

                //对每一个notification进行分类
                Iterator itea = notifiCations.iterator();
                while(itea.hasNext()){

                    NotifiCation notifiCation = (NotifiCation) itea.next();
                    //  type == 1  则说明是对一级评论进行的回复   不同的type对应不同的查询语句
                    if((notifiCation.getType() == 1) && (notifiCation.getStatus() == 0)){
                        NotifiCationDTO notifiCationDTO = new NotifiCationDTO();
                        //  有可能查出来的为空
                        QuestionDTO quession = questionService.queryQuestionDtoByQuestionId(notifiCation.getOuterid());
                        if(quession != null){
                            // 查出每一个notification对应的notifier
                            User u = userService.queryUserByUserId(notifiCation.getNotifier());
                            notifiCationDTO.setNotifierUser(u);
                            notifiCationDTO.setQuestionDTO(quession);
                            notificationQuestion.add(notifiCationDTO);
                        }else{
                            System.out.println("IndexController中查询的quession为空");
                        }

                    }

                    // type == 2 则说明是对二级回复进行的评论
                    if((notifiCation.getType() == 2) && (notifiCation.getStatus() == 0)){

                        NotifiCationDTO notifiCationDTO = new NotifiCationDTO();
                        Comment comment = commentService.queryCommentOneById(notifiCation.getOuterid());
                        User u = userService.queryUserByUserId(notifiCation.getNotifier());
                        notifiCationDTO.setNotifierUser(u);
                        notifiCationDTO.setComment(comment);

                        notificationComment.add(notifiCationDTO);

                    }

                }
            }

            HttpSession session = request.getSession();
            session.setAttribute("notificationQuestion",notificationQuestion);
            session.setAttribute("notificationComment",notificationComment);
            session.setAttribute("notifiCations",notifiCations);
            session.setAttribute("unreadSize",unreadSize);

        }

        //-----------------------------首页上的数据显示处理-------------------------------------------
        // 1. 查出总的记录数  -- 需要记录数
        List<Quession> quessionList = questionService.queryQuestionAll();

        // number 数量为总的页码数
        int number = quessionList.size()%7 ==0?quessionList.size()/7:quessionList.size()/7+1;
        if(pageno<1){
            pageno = 1;
        }
        if(pageno>number){
            pageno = number;
        }

        // 给page封装属性
        Page page = new Page(pageno,pagesize);

        //  设置总的问题 那么当前页码也会算出来
        page.setTotalsize(quessionList.size());

        // 对pageno进行出来了

        // 应该只查询出前面六条数据
        List<QuestionDTO> dtos = questionService.queryQuestionDtoAll(pageno,pagesize);

        page.setList(dtos);

        page.setPages();

        request.setAttribute("page", page);


        return "index";
    }


    /**
     *   搜索:
     *         跳转到index
     */
    @RequestMapping("/searchFunction")
    public String searchQuestionIndex(@RequestParam(value = "pageno",required = false ,defaultValue = "1") Integer pageno,
                                      @RequestParam(value = "pagesize",required = false,defaultValue = "7") Integer pagesize,
                                      HttpServletRequest request,
                                      String search,
                                      Model model){


        model.addAttribute("search",search);


        System.out.println(" 测试数据方法是否执行了");

        Result result = new Result();

        // 该questionList 应该是要显示的list集合  也就是该SQL语句要修改

        List<Quession> quessionList = questionService.queryQuestionAllBySearch(search);

        for (Quession quession : quessionList) {
            System.out.println("测试模糊查询的总数据"+quession);
        }

        // number 数量为总的页码数
        int number = quessionList.size()%7 ==0?quessionList.size()/7:quessionList.size()/7+1;
        if(pageno<1){
            pageno = 1;
        }
        if(pageno>number){
            pageno = number;
        }

        // 给page封装属性
        Page page = new Page(pageno,pagesize);

        //  设置总的问题 那么当前页码也会算出来
        page.setTotalsize(quessionList.size());

        // 对pageno进行出来了

        // 应该只查询出前面六条数据

        List<QuestionDTO> dtos = questionService.queryQuestionDtoBySearchFuncation(pageno,pagesize,search);

        page.setList(dtos);

        page.setPages();

        request.setAttribute("page", page);
        model.addAttribute("page",page);

        return "indexSearch";
    }


}
