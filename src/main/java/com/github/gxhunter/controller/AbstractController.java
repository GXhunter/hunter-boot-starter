package com.github.gxhunter.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.gxhunter.enums.ResultEnum;
import com.github.gxhunter.exception.ApiException;
import com.github.gxhunter.vo.Result;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AliasFor;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.Serializable;
import java.lang.annotation.*;
import java.lang.invoke.MethodHandle;
import java.util.HashMap;
import java.util.List;

/**
 * @author 树荫下的天空
 * @date 2019/2/25 16:27
 */
public class AbstractController<M extends IService<E>,E> extends BaseController{
    @Autowired(required = false)
    protected M mService;

    public Result create(E entity){
        return mService.save(entity) ? success():faild();
    }

    public Result deleteById(Serializable id){
        return mService.removeById(id)? success():faild();
    }

    public Result update(E entity){
        return mService.updateById(entity)? success(): faild();
    }

    public Result listAll(){
        return success(mService.list());
    }

    public Result<IPage<E>> page(Integer pageNum,Integer pageSize){
        Page<E> page = new Page<>(pageNum,pageSize);
        return success(mService.page(page));
    }

    public Result<E> getById(Serializable id){
        return success(mService.getById(id));
    }


    /**
     * 手动抛出的异常，不指定错误码时为“操作失败”
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(ApiException.class)
    public Object handServerException(ApiException exception){
        if(exception.getErrorCode() != null){
            return new Result<>(null,exception.getErrorCode().getMsg(),exception.getErrorCode().getCode());
        }else{
            return new Result<>(null,exception.getMessage(),ResultEnum.DEFAULT_ERROR.getCode());
        }
    }

    /**
     * 参数校验失败
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object resolveMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        List<ObjectError> objectErrors = ex.getBindingResult().getAllErrors();
        HashMap<String, String> resultMap = Maps.newHashMap();
        if(!CollectionUtils.isEmpty(objectErrors)){
            for(ObjectError objectError : objectErrors){
                String field = ((FieldError) objectError).getField();
                String message = objectError.getDefaultMessage();
                resultMap.put(field,message);
            }
        }
        return new Result<>(resultMap,ResultEnum.METHOD_ARGUMENT_VALID_FAIL.getMsg(),ResultEnum.METHOD_ARGUMENT_VALID_FAIL.getCode());
    }

    /**
     * 不可预料的异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public Object exceptionHandle(MethodHandle handle,Exception e){
        log.error("出现异常:",e);
        return new Result<>(null,ResultEnum.UNKNOW_ERROR.getMsg(),ResultEnum.UNKNOW_ERROR.getCode());
    }

    @Target(ElementType.METHOD)
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    protected @interface IfException{
        /**
         * @return 返回给前端的提示信息，与{{@link #errorMessage()}}相同
         */
        @AliasFor(value = "errorMessage")
        String value() default "";

        /**
         * @return 与{{@link #value()}}相同
         */
        @AliasFor(value = "value")
        String errorMessage() default "";
        /**
         * @return 捕获哪些异常，默认为{{@link Exception}}
         */
        Class<? extends Exception>[] when() default Exception.class;
    }
}
