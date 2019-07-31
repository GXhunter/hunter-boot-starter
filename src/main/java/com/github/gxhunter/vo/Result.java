package com.github.gxhunter.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.gxhunter.enums.IResponseCode;
import lombok.Data;

/**
 * @author 树荫下的天空
 * @date 2018/5/31 14:49
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result<T> implements IResponseCode{
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
