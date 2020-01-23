package com.example.myboot07.dto;

import com.example.myboot07.bean.User;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 *  该类用于传输数据
 */

@Data
public class CommentDTO implements Serializable {

    private Integer id;
    private Integer parent_id;
    private Integer type;
    private String comment;
    private Integer commentator;
    private String gmt_creat;
    private String gmt_modified;
    private Integer reply;
    // 将user也结合进来
    private User user;

}
