package com.example.myboot07.bean;

import lombok.Data;

/**
 * @author lenovo
 */
@Data
public class QQBean {

    private String client_id;
    private String client_secret;
    private String code;
    private String redirect_uri;
    private String state;


}
