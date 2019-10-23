package com.github.gxhunter.controller;

import com.github.gxhunter.exception.ApiException;
import com.github.gxhunter.jackson.ResultSupport;
import com.github.gxhunter.result.AResult;
import com.github.gxhunter.result.IResult;
import com.github.gxhunter.util.SpelPaser;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * @author 树荫下的天空
 * @date 2018.12.21
 */
public abstract class BaseController{
    @Autowired
    protected ResultSupport mResultSupport;
    private static final SpelPaser SPEL_PASER = SpelPaser.builder().regExp("#\\{[\\w.\\d]+}").build();
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
    protected <T> IResult<T> success(T entity){
        AResult<T> result = mResultSupport.success().clone();
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
        AResult result = mResultSupport.success().clone();
        result.setCode(mResultSupport.success().getCode());
        result.setMessage(message);
        return result;
    }

    /**
     * 返回失败
     *
     * @return
     */
    protected IResult faild(){
        AResult result = mResultSupport.faild().clone();
        result.setMessage(mResultSupport.faild().getMessage());
        result.setCode(mResultSupport.faild().getCode());
        return result;
    }

    protected IResult faild(Object data){
        AResult result = mResultSupport.faild().clone();
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
    protected IResult faild(String message){
        AResult result = mResultSupport.faild().clone();
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
    protected IResult faild(IResult errorCode){
        AResult result = mResultSupport.faild().clone();
        result.setMessage(errorCode.getMessage());
        result.setCode(errorCode.getCode());
        result.setData(errorCode.getData());
        return result;
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
        AResult result = mResultSupport.exception().clone();
        if(exception.getErrorCode() != null){
            return faild(exception.getErrorCode());
        }else{
            result.setCode(mResultSupport.exception().getCode());
            result.setMessage(exception.getMessage());
            return faild(result);
        }
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object handMethodValidException(MethodArgumentNotValidException ex){
        List<ObjectError> objectErrors = ex.getBindingResult().getAllErrors();
        HashMap<String, String> resultMap = Maps.newHashMap();
        if(!CollectionUtils.isEmpty(objectErrors)){
            for(ObjectError objectError : objectErrors){
                String field = ((FieldError) objectError).getField();
                String message = objectError.getDefaultMessage();
                resultMap.put(field,message);
            }
        }
        AResult result = mResultSupport.exception().clone();
        result.setData(resultMap);
        return result;
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
        AResult result = mResultSupport.exception().clone();
        String message = Optional.ofNullable(mResultSupport.exception().getMessage())
                .orElse(e.getMessage());
        result.setMessage(message);
        return result;
    }

}
