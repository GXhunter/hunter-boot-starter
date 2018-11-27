package com.github.gxhunter.exception;

import com.baomidou.mybatisplus.extension.exceptions.ApiException;
import com.github.gxhunter.dto.Result;
import com.github.gxhunter.enums.ResultEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;

/**
 * 全局异常处理器
 * 继承此类覆盖方法
 *
 * @author hunter
 * @date 2017/11/30  18:33
 */
@Slf4j
@RestControllerAdvice
@ConditionalOnClass(RestControllerAdvice.class)
@ConditionalOnMissingBean(ExceptionResolver.class)
@ConditionalOnWebApplication
public class ExceptionResolver{
    /**
     * 手动抛出的异常，不指定错误码时为“操作失败”
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(ApiException.class)
    public Result handServerException(ApiException exception){
        if(exception.getErrorCode() != null){
            return Result.failed(exception.getErrorCode());
        }else{
            return Result.failed(exception.getMessage());
        }
    }

    /**
     * 参数校验失败
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result resolveMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        List<ObjectError> objectErrors = ex.getBindingResult().getAllErrors();
        HashMap<String, String> resultMap = new HashMap<>();
        if(!CollectionUtils.isEmpty(objectErrors)){
            for(ObjectError objectError : objectErrors){
                String field = ((FieldError) objectError).getField();
                String message = objectError.getDefaultMessage();
                resultMap.put(field,message);
            }
            return Result.failed(resultMap,ResultEnum.METHOD_ARGUMENT_VALID_FAIL);
        }
        return Result.failed(ResultEnum.METHOD_ARGUMENT_VALID_FAIL);
    }

    /**
     * 不可预料的异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public Result exceptionHandle(Exception e){
        log.error("出现异常:",e);
        return Result.failed(ResultEnum.UNKNOW_ERROR);
    }


}
