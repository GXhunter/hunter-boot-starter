package com.github.gxhunter.controller;

import com.github.gxhunter.enums.IResult;
import com.github.gxhunter.exception.ApiException;
import com.github.gxhunter.exception.ClassifyException;
import com.github.gxhunter.jackson.IResultCodeAware;
import com.github.gxhunter.util.SpelPaser;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.annotation.PostConstruct;
import java.lang.annotation.*;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author 树荫下的天空
 * @date 2018.12.21
 */
public abstract class BaseController{
    @Autowired(required = false)
    protected IResultCodeAware mBaseResultCode;
    private static final SpelPaser SPEL_PASER = SpelPaser.builder().regExp("#\\{[\\w.\\d]+}").build();
    /**
     * 日志打印
     */
    protected final Logger log = LoggerFactory.getLogger(getClass());
    private IResult mResult;
    @PostConstruct
    void initResult(){
        try{
            mResult = mBaseResultCode.resultClass().newInstance();
        }catch(InstantiationException | IllegalAccessException e){
            e.printStackTrace();
        }
    }
    /**
     * 返回成功，并携带数据
     *
     * @param entity 携带数据
     * @return
     */
    protected <T> IResult<T> success(T entity){
        IResult<T> result = mResult.clone();
        result.setCode(mBaseResultCode.success().getCode());
        result.setMessage(mBaseResultCode.success().getMessage());
        result.setData(entity);
        return result;
    }

    /**
     * 返回成功
     *
     * @return
     */
    protected <T> IResult<T> success(){
        return success(null);
    }

    /**
     * 返回成功，并携带提示信息
     *
     * @param message
     * @return
     */
    protected IResult successMsg(String message){
        IResult result = mResult.clone();
        result.setCode(mBaseResultCode.success().getCode());
        result.setMessage(message);
        return result;
    }

    /**
     * 返回失败
     *
     * @return
     */
    protected IResult faild(){
        IResult result = mResult.clone();
        result.setMessage(mBaseResultCode.faild().getMessage());
        result.setCode(mBaseResultCode.faild().getCode());
        return result;
    }

    /**
     * 返回失败
     *
     * @param message 错误信息
     * @return
     */
    protected IResult faild(String message){
        IResult result = mResult.clone();
        result.setMessage(message);
        result.setCode(mBaseResultCode.faild().getCode());
        return result;
    }


    /**
     * 返回失败
     *
     * @param errorCode
     * @return
     */
    protected IResult faild(IResult errorCode){
        IResult result = mResult.clone();
        result.setMessage(errorCode.getMessage());
        result.setCode(mBaseResultCode.faild().getCode());
        return result;
    }


    /**
     * 加在Controller上自动捕获分类目标异常，并封装成 {@link ClassifyException}交由异常处理器处理，
     * 可继承{@link BaseController}重写{@link #handleClassifyException(ClassifyException)}方法自定义处理逻辑。
     */
    @Target(ElementType.ANNOTATION_TYPE)
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    protected @interface IfException{
        /**
         * @return 返回给前端的提示信息
         */
        @AliasFor(value = "errorMessage")
        String value() default "";

        /**
         * @return 错误码 默认为{@link } 默认错误码
         */
        int code() default -1;

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
        log.error(exception.getMessage(),exception);
        IResult result = mResult.clone();
        if(exception.getErrorCode() != null){
            return exception.getErrorCode();
        }else{
            result.setCode(mBaseResultCode.exception().getCode());
            result.setMessage(exception.getMessage());
            return result;
        }
    }

    /**
     * 捕获分类异常（对应Controller加上{{@link IfException}}）注解分类到的异常。
     * 重写此方法 重定义{@link IfException}捕获到异常后 的处理逻辑
     *
     * @param ex {@link IfException}捕获到的异常
     * @return 返回给前端的json数据
     */
    @ExceptionHandler(ClassifyException.class)
    public Object handleClassifyException(ClassifyException ex){
        log.error(ex.getExceptionClass().getName() + ":" + ex.getMessage(),ex);
        return faild(ex.getExceptionInfo());
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
        IResult result = mResult.clone();
        result.setMessage(mBaseResultCode.faild().getMessage());
        result.setCode(mBaseResultCode.exception().getCode());
        return result;
    }

    static List<IfExceptionInfo> getIfExceptionList(Method method,Object[] arguments){
        SPEL_PASER.setContext(method,arguments);
        List<IfExceptionInfo> result = Lists.newArrayList();
        ExceptionList exceptionList = method.getAnnotation(ExceptionList.class);
        if(exceptionList != null){
            for(IfException exception : exceptionList.value()){
                String value = SPEL_PASER.parse(exception.value());
                result.add(new IfExceptionInfo(value,exception.code(),exception.on()));
            }
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
         * @return 错误码 默认为{@link } 默认错误码
         */
        Integer code;

        /**
         * @return 捕获哪些异常，默认为{{@link Exception}}
         */
        Class<? extends Exception>[] when;
    }
}
