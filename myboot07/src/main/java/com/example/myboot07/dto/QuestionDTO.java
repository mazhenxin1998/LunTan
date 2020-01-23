package com.example.myboot07.dto;


import com.example.myboot07.bean.User;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

/**
 *    数据传输对象
 */

@Data
public class QuestionDTO {

    private String title;
    private String description;
    private String text;
    private String gmt_creat;
    private String gmt_modified;
    private Integer creator;  // 当前用户的姓名
    private Integer comment_count;
    private Integer view_count;
    private Integer like_count;
    private String tag;
    private Integer id;
    //  由于要关联User  所以在这里引入User对象
    private User user;

}
