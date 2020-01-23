package com.example.myboot07.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 封装access——token数据 进行传输
 */

@Getter
@Setter
@ToString
public class AccessTokenDTO {

    private String client_id;
    private String client_secret;
    private String code;
    private String redirect_uri;
    private String state;

}
