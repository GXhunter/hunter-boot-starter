package com.github.gxhunter.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.gxhunter.enums.IResponseCode;
import com.github.gxhunter.enums.ResultEnum;
import lombok.Data;

/**
 * @author 树荫下的天空
 * @date 2018/5/31 14:49
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result<T>{
    private long status;
    private String message;
    T data;

    public Result(){}

    private Result(T data,String message,long status){
        this.data = data;
        this.message = message;
        this.status = status;
    }

    /**
     * 本次请求是否成功
     *
     * @return true 成功  false 失败
     */
    public boolean isSuccess(){
        return this.status == ResultEnum.SUCCESS.getCode();
    }


    /**
     * 创建成功返回类，携带数据和提示信息
     *
     * @param data    携带的数据
     * @param message 返回的信息
     * @param <T>     数据类型
     * @return
     */
    public static <T> Result<T> success(T data,String message){
        return new Result<T>(data,message,ResultEnum.SUCCESS.getCode());
    }

    /**
     * 创建成功返回类
     *
     * @param data 携带的对象
     * @param <T>  数据类型
     * @return
     */
    public static <T> Result<T> success(T data){
        return success(data,null);
    }

    /**
     * @param <T>  数据类型
     * @return
     */
    public static <T> Result<T> success(){
        return success(null);
    }

    /**
     * 创建带提示信息的成功返回类（不携带data数据）
     *
     * @param message
     * @param <T>  数据类型
     * @return
     */
    public static <T> Result<T> successMsg(String message){
        return success(null,message);
    }


    /**
     * 创建失败对象，携带对象和提示信息
     *
     * @param data      数据内容
     * @param message   数据信息
     * @param <T>       数据类型
     * @return
     */
    public static <T> Result<T> failed(T data,String message){
        return new Result<T>(data,message,ResultEnum.DEFAULT_ERROR.getCode());
    }

    /**
     * 创建失败对象，携带对象和提示信息
     *
     * @param data      数据内容
     * @param <T>       数据类型
     * @return
     */
    public static <T> Result<T> failed(T data,IResponseCode errorCode){
        return new Result<T>(data,errorCode.getMsg(),errorCode.getCode());
    }

    /**
     * 创建返回失败对象，携带提示信息
     *
     * @param message 提示信息
     * @param <T>     数据类型
     * @return
     */
    public static <T> Result<T> failed(String message){
        return failed(null,message);
    }

    public static <T> Result<T> failed(){
        return failed((String) null);
    }

    public static <T> Result<T> failed(IResponseCode resultEnum){
        return new Result<T>(null,resultEnum.getMsg(),resultEnum.getCode());
    }

}
