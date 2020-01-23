package com.example.myboot07.bean;

import com.example.myboot07.dto.CommentDTO;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 *  该类表示前后台传递的状态类
 */
@Data
@ToString
public class Result {

    // 状态码 返回到前台页面进行校验的基准
    private Integer code;
    private boolean success;
    private String message;
    private List<Object> data = null;
    private List<CommentDTO> commentDTOS = null;
    public static Result errorOf(Integer code,String message){


        Result result = new Result();

        result.setCode(code);
        result.setMessage(message);

        return result;
    }

    public static Result okOf(Integer code,String message){

        Result result = new Result();
        result.setCode(code);
        result.setMessage(message);
        return result;

    }

}
