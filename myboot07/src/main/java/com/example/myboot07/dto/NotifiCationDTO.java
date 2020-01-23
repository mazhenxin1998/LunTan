package com.example.myboot07.dto;

import com.example.myboot07.bean.Comment;
import com.example.myboot07.bean.Quession;
import com.example.myboot07.bean.User;
import lombok.Data;

@Data
public class NotifiCationDTO {

    private Integer id;
    private Integer notifier;
    private Integer receiver;
    private Integer outerid;
    private Integer commentid;
    private Integer type;
    private Integer status;
    private String gmt_create;

    private Comment comment;
    private QuestionDTO questionDTO;

    private User receiverUser;
    private User notifierUser;


}
