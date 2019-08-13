package com.github.gxhunter.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.gxhunter.enums.AResult;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

/**
 * 继承自{{@link BaseController}} 包含默认的异常处理，并支持{{@link com.github.gxhunter.controller.BaseController.IfException}}注解
 * @author 树荫下的天空
 * @param <M>
 * @param <E>
 *
 */
public class MPController<M extends IService<E>,E> extends BaseController{
    @Autowired(required = false)
    protected M mService;

    public AResult create(E entity){
        return mService.save(entity) ? success() : faild();
    }

    public AResult deleteById(Serializable id){
        return mService.removeById(id) ? success() : faild();
    }

    public AResult update(E entity){
        return mService.updateById(entity) ? success() : faild();
    }

    public AResult listAll(){
        return success(mService.list());
    }

    public AResult<IPage<E>> page(Integer pageNum,Integer pageSize){
        Page<E> page = new Page<>(pageNum,pageSize);
        return success(mService.page(page));
    }

    public AResult<E> getById(Serializable id){
        return success(mService.getById(id));
    }

}
