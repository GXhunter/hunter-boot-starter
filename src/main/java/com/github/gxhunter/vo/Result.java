package com.github.gxhunter.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.gxhunter.enums.IResponseCode;
import com.github.gxhunter.enums.ResultEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * @author 树荫下的天空
 * @date 2018/5/31 14:49
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result<T>{
    private Serializable code;
    private String message;
    private T data;

    public Result(){}

    public Result(T data,String message,Serializable code){
        this.data = data;
        this.message = message;
        this.code = code;
    }

    /**
     * 本次请求是否成功
     *
     * @return true 成功  false 失败
     */
    public boolean getFlag(){
        return this.code.equals(ResultEnum.SUCCESS.getCode());
    }

}
