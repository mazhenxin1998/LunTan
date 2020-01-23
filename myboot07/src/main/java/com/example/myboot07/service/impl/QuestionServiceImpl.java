package com.example.myboot07.service.impl;

import com.example.myboot07.bean.Quession;
import com.example.myboot07.bean.User;
import com.example.myboot07.dao.IQussionDao;
import com.example.myboot07.dao.IUserDao;
import com.example.myboot07.dto.QuestionDTO;
import com.example.myboot07.service.IQuestionService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.LinkedList;
import java.util.List;

@Service
public class QuestionServiceImpl implements IQuestionService {

    @Autowired
    private IQussionDao qussionDao;

    @Autowired
    private IUserDao userDao;


    @Override
    public Integer saveQuestion(Quession quession) {

        Integer count = qussionDao.saveQuestion(quession);

        return count;
    }

    @Override
    public List<Quession> queryQuestionAllBySearch(String search) {

        //   根据search模糊查询出  符合条件的问题
        List<Quession> list = qussionDao.queryQuestionAllBySearch(search);
        if(list == null){
            //   应该抛出异常
            return null;
        } else {
            return list;
        }

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<QuestionDTO> queryQuestionDtoBySearchFuncation(Integer pageno, Integer pagesize,String search) {

        int stardIndex = (pageno-1)*pagesize;

        //  查询出来的是前八条数据
        List<Quession> quessionList = qussionDao.queryQuestionBySearchFunction(stardIndex,pagesize,search);

        // 声明QuestionDTO
        List<QuestionDTO> list = new LinkedList<QuestionDTO>();
        // 将所有的question和其对应的user信息查出来
        for (Quession quession : quessionList) {
            QuestionDTO dto = new QuestionDTO();

            User user = userDao.queryUserByUserId(quession.getCreator());
            // spring的工具类  类与类之间复制对象
            BeanUtils.copyProperties(quession, dto);

            dto.setUser(user);

            list.add(dto);
        }

        return list;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer updateQuestionLikeCount(Integer id) {

        Integer count = qussionDao.updateQuestionLikeCount(id);
        if(count == 1){
            return count;
        }else{
            // 正常来说应该抛出异常
            return null;
        }

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer deleteQuestionById(Integer id) {

        Integer count = qussionDao.deleteQuestionById(id);

        if(count == 1){
            // 如果删除成功  那么应该将该问题所含有的所有回复删除(暂时不做处理)

            return count;
        }else{
            //  失败 应该抛出异常(暂时未处理)
            return null;
        }

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer queryMyQuestionSize(Integer id) {

        Integer count = qussionDao.queryMyQuestionSize(id);

        if(count == null){
            // 抛出异常
            return null;
        }else{
            return count;
        }

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer updateQuestionViewCount(Integer id) {

        Integer count = qussionDao.updateQuestionViewCount(id);

        if( count == 1){
            // 用户浏览成功
            return count;

        }else{
            // 如果前台传过来的ID在数据库中不存在，那么应该抛出问题不存在异常
            // 这里调用枚举中的静态常量
            return null;
        }

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<Quession> queryQuestionDtoAllByCreator(Integer pageno, Integer pagesize, Integer id) {

        // 设置查找的起始坐标
        int starIndex = pagesize*(pageno-1);

        List<Quession> list = qussionDao.queryQuestionDtoAllByCreator(starIndex,pagesize,id);

        return list;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer updateQuestion(Quession quession) {

        Integer count = null;

        try {
            count = qussionDao.updateQuestion(quession);
        }catch (Exception e){
            //  应该自定义异常
            System.out.println("问题更新是出现异常");
        }

        return count;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public QuestionDTO queryQuestionDtoByQuestionId(Integer id) {

        //  通过ID查出一个确定的Question
        // 通过该Question 来查出其对应的user

        Quession quession = qussionDao.queryQuestionByID(id);

        if(quession == null){
            // 应该抛出异常
            System.out.println("QuestionServiceImpl层抛出的异常---在查找问题的时候");
            return null;
        }else{
            // 通过question来查出是谁发布的
            // 测试数据
            User user = userDao.queryUserByUserId(quession.getCreator());
            if(user == null){
                System.out.println("当前问题的创造者为空");
            }
            //  将二者组成DTO类型  --- 利用spring的工具类
            QuestionDTO questionDTO = new QuestionDTO();

            BeanUtils.copyProperties(quession,questionDTO);
            // 如果question  为空,  那么这里就出错
            questionDTO.setCreator(quession.getCreator());

            if(questionDTO == null){
                System.out.println("questionDTO为空");
            }

            // 最后将user也放入到当前的questionDTO中
            questionDTO.setUser(user);
            return questionDTO;

        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<Quession> queryQuestionAll() {
        return qussionDao.queryQuestionAll();
    }

    /**
     * 返回一个QuestionDTO  集合， 用于传输数据 方便在前台展示
     * 思路：
     * 1. 查询出所有的question集合
     * 2. 给每一个question查出其对应的user信息
     * 3. 将两者封装到question中
     *
     *
     *      范围：
     *          该类还可以作为分页service模板  建议在重新写一遍
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<QuestionDTO> queryQuestionDtoAll(@RequestParam(value = "pageno",required = false,defaultValue = "1") int pageno,
                                                 @RequestParam(value = "pagesize" ,required = false,defaultValue = "7") int pagesize) {
        // 查出所有的question数据确定页数
        List<Quession> quessions =qussionDao.queryQuestionAll();  // 查出所有数据
        Integer count = quessions.size();
        int stardIndex = (pageno-1)*pagesize;

        //  查询出来的是前7条数据
        List<Quession> quessionList = qussionDao.queryQuestionPage(stardIndex,pagesize);

        // 声明QuestionDTO
        List<QuestionDTO> list = new LinkedList<QuestionDTO>();
        // 将所有的question和其对应的user信息查出来
        for (Quession quession : quessionList) {
            QuestionDTO dto = new QuestionDTO();

            User user = userDao.queryUserByUserId(quession.getCreator());
            // spring的工具类  类与类之间复制对象
            BeanUtils.copyProperties(quession, dto);

            dto.setUser(user);

            list.add(dto);
        }

        return list;
    }
}
