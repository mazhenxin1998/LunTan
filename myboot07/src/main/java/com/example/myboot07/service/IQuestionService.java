package com.example.myboot07.service;

import com.example.myboot07.bean.Quession;
import com.example.myboot07.dto.QuestionDTO;

import java.util.List;

public interface IQuestionService {


    Integer saveQuestion(Quession quession);

    List<QuestionDTO> queryQuestionDtoAll(int pageno,int pagesize);

    List<Quession> queryQuestionAll();

    QuestionDTO queryQuestionDtoByQuestionId(Integer id);

    Integer updateQuestion(Quession quession);

    List<Quession> queryQuestionDtoAllByCreator(Integer pageno, Integer pagesize, Integer id);

    Integer updateQuestionViewCount(Integer id);

    Integer queryMyQuestionSize(Integer id);

    Integer deleteQuestionById(Integer id);

    Integer updateQuestionLikeCount(Integer id);

    List<QuestionDTO> queryQuestionDtoBySearchFuncation(Integer pageno, Integer pagesize,String serarch);

    List<Quession> queryQuestionAllBySearch(String search);
}
