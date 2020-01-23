package com.example.myboot07.bean;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Data
@Getter
@Setter
@ToString
public class NotifiCation implements Serializable {

    private Integer id;
    private Integer notifier;
    private Integer receiver;
    private Integer outerid;
    private Integer type;
    private Integer status;
    private String gmt_create;

}
