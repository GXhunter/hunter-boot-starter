package com.github.gxhunter.controller;

import com.github.gxhunter.enums.ResultEnum;
import com.github.gxhunter.exception.ApiException;
import com.github.gxhunter.exception.ClassifyException;
import com.github.gxhunter.vo.Result;
import com.github.gxhunter.enums.IResponseCode;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.lang.annotation.*;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author 树荫下的天空
 * @date 2018.12.21
 */
public abstract class BaseController{
    /**
     * 日志打印
     */
    protected final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * 返回成功，并携带数据
     *
     * @param entity 携带数据
     * @return
     */
    protected <T> Result<T> success(T entity){
        return new Result<>(entity,ResultEnum.SUCCESS.getMsg(),ResultEnum.SUCCESS.getCode());
    }

    /**
     * 返回成功
     *
     * @return
     */
    protected <T> Result<T> success(){
        return new Result<>(null,ResultEnum.SUCCESS.getMsg(),ResultEnum.SUCCESS.getCode());
    }

    /**
     * 返回成功，并携带提示信息
     *
     * @param message
     * @return
     */
    protected Result successMsg(String message){
        return new Result<>(null,message,ResultEnum.SUCCESS.getCode());
    }

    /**
     * 返回失败
     *
     * @return
     */
    protected Result faild(){
        return new Result<>(null,ResultEnum.DEFAULT_ERROR.getMsg(),ResultEnum.DEFAULT_ERROR.getCode());
    }

    /**
     * 返回失败
     *
     * @param message 错误信息
     * @return
     */
    protected Result faild(String message){
        return new Result<>(null,message,ResultEnum.DEFAULT_ERROR.getCode());
    }


    /**
     * 返回失败
     *
     * @param errorCode
     * @return
     */
    protected Result faild(IResponseCode errorCode){
        return new Result<>(null,errorCode.getMsg(),errorCode.getCode());
    }


    /**
     * 加在Controller上自动捕获分类目标异常，并封装成 {@link ClassifyException}交由异常处理器处理，
     * 可继承{@link BaseController}重写{@link #handleClassifyException(ClassifyException)}方法自定义处理逻辑。
     */
    @Target(ElementType.METHOD)
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Repeatable(value = ExceptionList.class)
    protected @interface IfException{
        /**
         * @return 返回给前端的提示信息
         */
        @AliasFor(value = "errorMessage")
        String value() default "";

        /**
         * @return 错误码 默认为{@link ResultEnum} 默认错误码
         */
        long code() default -1L;

        /**
         * @return 捕获哪些异常，默认为{@link Exception}
         */
        Class<? extends Exception>[] on() default Exception.class;

    }

    @Target(ElementType.METHOD)
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    protected @interface ExceptionList{
        IfException[] value();
    }

    /**
     * 手动抛出的异常，不指定错误码时为“操作失败”
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(ApiException.class)
    public Object handleApiException(ApiException exception){
        if(exception.getErrorCode() != null){
            return new Result<>(null,exception.getErrorCode().getMsg(),exception.getErrorCode().getCode());
        }else{
            return new Result<>(null,exception.getMessage(),ResultEnum.DEFAULT_ERROR.getCode());
        }
    }

    /**
     * 捕获分类异常（对应Controller加上{{@link IfException}}）注解分类到的异常。
     * 重写此方法 重定义{@link IfException}捕获到异常后 的处理逻辑
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(ClassifyException.class)
    public Object handleClassifyException(ClassifyException ex){
        log.error(ex.getExceptionClass().getName() + ":" + ex.getMessage(),ex);
        return new Result<>(null,ex.getErrorCode().getMsg(),ex.getErrorCode().getCode());
    }

    /**
     * 其他异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public Object handleOtherException(Exception e){
        log.error("出现异常:",e);
        return new Result<>(null,ResultEnum.UNKNOW_ERROR.getMsg(),ResultEnum.UNKNOW_ERROR.getCode());
    }

    static List<IfExceptionInfo> getIfExceptionList(Method method){
        List<IfExceptionInfo> result = Lists.newArrayList();
        for(IfException ifException : method.getAnnotation(ExceptionList.class).value()){
            result.add(new IfExceptionInfo(ifException.value(),ifException.code(),ifException.on()));
        }
        return result;
    }

    @AllArgsConstructor
    @Getter
    static class IfExceptionInfo{
        /**
         * @return 返回给前端的提示信息
         */
        String value;

        /**
         * @return 错误码 默认为{@link ResultEnum} 默认错误码
         */
        long code;

        /**
         * @return 捕获哪些异常，默认为{{@link Exception}}
         */
        Class<? extends Exception>[] when;
    }
}
