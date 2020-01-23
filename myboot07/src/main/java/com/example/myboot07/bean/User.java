package com.example.myboot07.bean;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigInteger;

/**
 *  User所对应的的实体类
 *
 *      该User  对应GitHub的User
 *
 *        "created_at": "2019-10-19T05:23:25Z",
 *   "updated_at": "2019-11-10T08:22:48Z"
 *
 */
@Data
public class User implements Serializable {

    // GitHub用户需要的部分

    private Integer id;
    private Integer account_id;
    private String name;
    private String token;
    private String gmt_creat;
    private String gmt_modified;

    // https://avatars0.githubusercontent.com/u/56750847?v=4

    private String avatar_url;
    private String created_at;
    private String updated_at;

    private String password;
    private String account;




}
