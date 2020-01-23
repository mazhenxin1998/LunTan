package com.example.myboot07.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class Comment implements Serializable {

    private Integer id;
    private Integer parent_id;
    private Integer type;
    private Integer commentator;
    private String gmt_creat;
    private String gmt_modified;
    private String comment;

    private Integer reply;



}
