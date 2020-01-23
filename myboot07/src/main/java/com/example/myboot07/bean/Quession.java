package com.example.myboot07.bean;

import javafx.beans.property.SimpleIntegerProperty;
import lombok.Data;

import java.math.BigInteger;

/**
 * @Data 包含 包含了get和set 和tostring
 */


@Data
public class Quession {


    private String title;
    private String description;
    private String gmt_creat;
    private String gmt_modified;
    private Integer creator;  // 当前用户的姓名
    private Integer comment_count;
    private Integer view_count;
    private Integer like_count;
    private String tag;
    private Integer id;


}
