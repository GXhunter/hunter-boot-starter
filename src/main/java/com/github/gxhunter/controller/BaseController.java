package com.github.gxhunter.controller;

import com.baomidou.mybatisplus.extension.exceptions.ApiException;
import com.github.gxhunter.dto.Result;
import com.github.gxhunter.enums.ResultEnum;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author 树荫下的天空
 * @date 2018/6/4 15:58
 */
public abstract class BaseController<T>{
    @Autowired
    protected T mService;

    /**
     * 返回成功数据
     *
     * @param valueObject 字符串类型：调用successMsg方法、非string类型使用success方法
     * @return
     */
    protected <M>Result<M> success(M valueObject){
        if(valueObject instanceof String){
            return Result.successMsg(((String) valueObject));
        }else{
            return Result.success(valueObject);
        }
    }

    protected Result success(){
        return Result.success();
    }

    protected Result failure(String message){
        throw new ApiException(message);
    }

    protected Result failure(){
        throw new ApiException(ResultEnum.DEFAULT_ERROR);
    }

    /**
     * 如果传入的对象为null、空集合、false 返回错误
     * 如果传入的是string，以message字段返回
     * 其他成功
     *
     * @param res
     * @param <M>
     * @return
     */
    protected <M> Result<M> response(M res){
        return response(res,ResultEnum.DEFAULT_ERROR);
    }

    /**
     * 指定返回内容和异常枚举
     * 自动根据内容决定返回成功或失败
     * @param res 成功时携带的数据
     * @param failEnum 失败时抛出的异常类型
     * @param <M>
     * @return
     */
    protected <M> Result<M> response(M res,ResultEnum failEnum){
        if(res == null){
            throw new ApiException(failEnum);
        }

        if(res instanceof Boolean){
            return ((Boolean) res) ? Result.success() : Result.failed();
        }

        if(res instanceof List){
            if(((List) res).size() == 0){
                throw new ApiException(failEnum);
            }
        }

        if(res instanceof String){
            return Result.successMsg(((String) res));
        }

        return Result.success(res);
    }

}
