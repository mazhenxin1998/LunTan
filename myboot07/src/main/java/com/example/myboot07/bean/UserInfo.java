package com.example.myboot07.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lenovo
 */

@Data
public class UserInfo implements Serializable {

    private Integer id;
    private String city;
    private String company;
    private String school;
    private String personalweb;
    private String intro;
    private Integer userid;

}
