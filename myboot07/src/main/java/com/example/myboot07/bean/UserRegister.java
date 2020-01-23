package com.example.myboot07.bean;


import lombok.Data;

import java.io.Serializable;

/**
 *  注册时用的user对象
 */
@Data
public class UserRegister implements Serializable {

    private String account;
    private String password;
    private String name;
    private String gmt_creat;
    private String gmt_modifiled;
    private String avatar_url;

}
