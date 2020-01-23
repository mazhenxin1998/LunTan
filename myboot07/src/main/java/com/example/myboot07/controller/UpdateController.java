package com.example.myboot07.controller;

import com.example.myboot07.bean.Comment;
import com.example.myboot07.bean.NotifiCation;
import com.example.myboot07.bean.Quession;
import com.example.myboot07.bean.User;
import com.example.myboot07.dto.CommentDTO;
import com.example.myboot07.dto.QuestionDTO;
import com.example.myboot07.service.ICommentService;
import com.example.myboot07.service.INotifiCationService;
import com.example.myboot07.service.IQuestionService;
import com.example.myboot07.service.IUserService;
import com.example.myboot07.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


/**
 * 对已经有的问题进行修改操作
 */

@Controller
public class UpdateController {

    @Autowired
    private IQuestionService questionService;

    @Autowired
    private ICommentService commentService;

    @Autowired
    private INotifiCationService notifiCationService;

    @Autowired
    private IUserService userService;

    /**
     * 注意： 客户有可能不需要修改其他东西 仅仅只想修改一个
     *
     * @param title
     * @param description
     * @param tag
     * @return
     */
    @RequestMapping("/update")
    public String updateQuestion(@RequestParam(value = "title", required = false) String title,
                                 @RequestParam(value = "description", required = false) String description,
                                 @RequestParam(value = "tag", required = false) String tag,
                                 @RequestParam(value = "id", required = false) Integer id) {

        //  设置更新时间
        String now = new Date().toLocaleString();

        // 将所有数据封装成一个user对象
        Quession quession = new Quession();
        quession.setGmt_modified(now);
        quession.setTag(tag);
        quession.setDescription(description);
        quession.setTitle(title);
        quession.setId(id);

        Integer count = questionService.updateQuestion(quession);

        if (count == 1) {
            //  更新成功

        } else {
            // 更新失败 如果更新失败 希望跳转到指定页面 或者做一些处理
            // 更新失败则抛出异常

        }


        return "redirect:/domyquestion";
    }


    /**
     * 先跳转到update页面 在更新
     * 由于要回显数据 所以要接受参数
     * title description tag
     * 要求返回必须有result
     * <p>
     * 注意： 要接受一个id(当前要修改问题的ID)
     */
    @RequestMapping("/toupdate")
    public String edit(String title, String description, Integer id,
                       Model model) {

        model.addAttribute("id", id);
        model.addAttribute("title", title);
        model.addAttribute("description", description);

        return "Update";
    }

    /**
     * 该方法处理由index页面点击标题进入Update页面
     * <p>
     * 也就是浏览页面 :  应该跳转到编辑页面
     * 没当该方法执行一次 那么就将该id对应的问题的浏览数增加一次
     * 注意： 每次增加一个  需要注意细节：
     * <p>
     * <p>
     * 注意 : 应该注意表单重复提交细节------遗留问题
     * <p>
     * <p>
     * ip  ： 每次进入该controller应该先对IP进行判断 如果存在IP则浏览数不+1  如果不存在则浏览数+1 同时将该IP放入到IP表中
     */
    @RequestMapping("/toUpdateFromIndex/{id}")
    public String toUpdateFromIndex(@PathVariable("id") Integer id,
                                    Model model,
                                    HttpServletRequest request,
                                    HttpSession session) {

        User user = (User) session.getAttribute("user");
        Integer count = 0;
        if(user != null){
            count = questionService.updateQuestionViewCount(id);
        }


        // 第二个页面 还需要展示该问题，所以应该将该id对应的信息加载到request域中
        QuestionDTO dto = questionService.queryQuestionDtoByQuestionId(id);

        // 同时查出该dto所有的相关问题------- 模糊查询
        String tag = dto.getTag();
        List<Quession> relatedQuestion = commentService.queryRelatedCommentByTag(tag);

        // 同时需要将该问题的所有回复放入到request域中  --- 这里应该做成分页查询
        //List<Comment> comments = commentService.queryCommentOnePage(id);    // 查询一页评论(暂时不完整稍后补充!)

        // 错误的
        List<CommentDTO> commentDTOS = commentService.queryCommentOnePage(id);

        if (count == 1 || count == 0) {
            model.addAttribute("dto", dto);
            // 将dto里面的tag标签分解之后，在传送到前台
            String t = dto.getTag();
            //  注意该,号需要区分中英文
            String[] tags = t.split(",");
            model.addAttribute("tags",tags);
            // 应为后面还需要改该问题的id 所以应该将该问题id放入到页面中
            model.addAttribute("id", id);

            // 将commentdto放入到request域中

            model.addAttribute("commentDTOS", commentDTOS);

            model.addAttribute("relatedQuestion",relatedQuestion);

            //------------前提是user不为空 并且user等于dto里面的user

            // -------------处理未读和已读状态问题  该Controller只处理type为1时的情况  发布该问题的人也就是RecevierUser

            // questionDTO 里面包含接受者的User信息

            // type =1 receiver 为questionDTO里面的User   outerid = id

            if(user != null && user.getId().equals(dto.getUser().getId())){
                Integer num = notifiCationService.updateNotificationStatus(1,dto.getUser(),id);
                System.out.println("测试是否更新成功："+num);
            }

            //----------------------------------------------------------------------------------

            return "DetailsProblemFromHp";
        } else {
            //  如果浏览失败，那么就先返回index页面
            return "redirect:/";
        }

    }






}
