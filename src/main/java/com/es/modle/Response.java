package com.es.modle;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Response {
    //状态码
    private Integer code;
    //返回信息
    private String message;
    //返回的数据
    private Object data;

}
