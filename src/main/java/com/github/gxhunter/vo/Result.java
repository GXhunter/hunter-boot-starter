package com.github.gxhunter.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.gxhunter.enums.IResult;
import lombok.Data;

/**
 * 微服务之间、前后端交互的基本对象
 * 序列化方法可自行在yml配置
 * @author 树荫下的天空
 * @date 2018/5/31 14:49
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result<T> extends IResult<T>{
    private Integer code;
    private String message;
    private T data;

    public Result(){}

    public Result(T data,String message,Integer code){
        this.data = data;
        this.message = message;
        this.code = code;
    }
}
