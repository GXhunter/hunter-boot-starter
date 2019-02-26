package com.github.gxhunter.controller;

import com.github.gxhunter.vo.Result;
import com.github.gxhunter.enums.IResponseCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 树荫下的天空
 * @date 2018.12.21
 */
public abstract class BaseController{
    /**
     * 日志打印
     */
    protected Logger log = LoggerFactory.getLogger(getClass());

    /**
     * 返回成功，并携带数据
     *
     * @param entity 携带数据
     * @return
     */
    protected <T> Result<T> success(T entity){
        return Result.success(entity);
    }
    /**
     * 返回成功
     *
     * @return
     */
    protected <T> Result<T> success(){
        return Result.success();
    }

    /**
     * 返回成功，并携带提示信息
     *
     * @param message
     * @return
     */
    protected Result successMsg(String message){
        return Result.successMsg(message);
    }

    /**
     * 返回失败
     *
     * @return
     */
    protected Result faild(){
        return Result.failed();
    }

    /**
     * 返回失败
     *
     * @param message 错误信息
     * @return
     */
    protected Result faild(String message){
        return Result.failed(message);
    }


    /**
     * 返回失败
     *
     * @param errorCode
     * @return
     */
    protected Result faild(IResponseCode errorCode){
        return Result.failed(errorCode);
    }


}
