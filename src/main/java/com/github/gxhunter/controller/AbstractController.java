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
        return mService.save(entity) ? success() : faild();
    }

    public Result deleteById(Serializable id){
        return mService.removeById(id) ? success() : faild();
    }

    public Result update(E entity){
        return mService.updateById(entity) ? success() : faild();
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





}
