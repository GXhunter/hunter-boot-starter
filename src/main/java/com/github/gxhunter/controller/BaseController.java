package com.github.gxhunter.controller;

import com.github.gxhunter.result.Result;
import com.github.gxhunter.result.ResultSupport;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author 树荫下的天空
 * @date 2018.12.21
 */
public abstract class BaseController{
    @Autowired
    protected ResultSupport mResultSupport;
    /**
     * 返回成功，并携带数据
     *
     * @param entity 携带数据
     * @return
     */
    protected final  <T> Result<T> success(T entity){
        Result<T> result = mResultSupport.success().clone();
        result.setCode(mResultSupport.success().getCode());
        result.setMessage(mResultSupport.success().getMessage());
        result.setData(entity);
        return result;
    }

    /**
     * 返回成功
     *
     * @return
     */
    protected final  <T> Result<T> success(){
        return success(null);
    }

    /**
     * 返回成功，并携带提示信息
     *
     * @param message
     * @return
     */
    protected final Result successMsg(String message){
        Result result = mResultSupport.success().clone();
        result.setCode(mResultSupport.success().getCode());
        result.setMessage(message);
        return result;
    }

    /**
     * 返回失败
     *
     * @return
     */
    protected final Result faild(){
        Result result = mResultSupport.faild().clone();
        result.setMessage(mResultSupport.faild().getMessage());
        result.setCode(mResultSupport.faild().getCode());
        return result;
    }

    protected final Result faild(Object data){
        Result result = mResultSupport.faild().clone();
        result.setMessage(mResultSupport.faild().getMessage());
        result.setCode(mResultSupport.faild().getCode());
        result.setData(data);
        return result;
    }

    /**
     * 返回失败
     *
     * @param message 错误信息
     * @return
     */
    protected final Result faild(String message){
        Result result = mResultSupport.faild().clone();
        result.setMessage(message);
        result.setCode(mResultSupport.faild().getCode());
        return result;
    }


    /**
     * 返回失败
     *
     * @param errorCode
     * @return
     */
    protected final Result faild(Result errorCode){
        Result result = mResultSupport.faild().clone();
        result.setMessage(errorCode.getMessage());
        result.setCode(errorCode.getCode());
        result.setData(errorCode.getData());
        return result;
    }



}
