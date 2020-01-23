package com.example.myboot07.bean;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;


@Data
@ToString
public class Tag implements Serializable {

    private Integer id;
    private String tag;
    private Integer tagcreator;
    private String type;
    private String tagId;


}
