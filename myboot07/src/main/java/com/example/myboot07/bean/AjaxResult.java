package com.example.myboot07.bean;


import com.example.myboot07.dto.QuestionDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 *  前后端交互的数据
 */
@Data
public class AjaxResult implements Serializable {

    // Object  更通用些
    private List<QuestionDTO> datas;
    private boolean success;
    private boolean isLogin;
    private String message;
    // code  作用：
    private Integer code;





}
