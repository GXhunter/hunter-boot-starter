package com.github.gxhunter.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.gxhunter.result.Result;
import com.github.gxhunter.result.ResultSupport;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <pre>
 * <h2>注意：在项目引入mybatis-plus后才可用</h2>
 * 继承自{@link BaseController} 包含默认的异常处理
 * 在spring容器中，提供一个{@link ResultSupport}，即可配置返回的对象
 * </pre>
 *
 * @param <M> 服务层
 * @param <E> 实体类
 * @author 树荫下的天空
 * @see BaseController
 * @see ResultSupport
 */
public class CrudController<M extends IService<E>,E> extends BaseController{
    @Autowired(required = false)
    protected M mService;

    /**
     * 新增，建议使用post请求
     *
     * @param entity
     * @return
     * @see org.springframework.web.bind.annotation.PostMapping
     * @see org.springframework.web.bind.annotation.RequestBody
     */
    public Result create(E entity){
        return mService.save(entity) ? success() : faild();
    }

    /**
     * 通过id删除，建议使用delete请求
     *
     * @param id
     * @return
     * @see org.springframework.web.bind.annotation.DeleteMapping
     */
    public Result deleteById(Long id){
        return mService.removeById(id) ? success() : faild();
    }

    /**
     * 新增或修改
     *
     * @param entity id不为空时修改，为空新增
     * @return
     */
    public Result saveOrUpdate(E entity){
        return mService.saveOrUpdate(entity) ? success() : faild();
    }

    /**
     * 修改
     *
     * @param entity 实体类
     * @return
     * @see org.springframework.web.bind.annotation.PutMapping
     * @see org.springframework.web.bind.annotation.RequestBody
     */
    public Result update(E entity){
        return mService.updateById(entity) ? success() : faild();
    }

    /**
     * 查询所有,建议使用get请求
     *
     * @return
     * @see org.springframework.web.bind.annotation.GetMapping
     */
    public Result listAll(){
        return success(mService.list());
    }

    /**
     * 分页查询，建议使用get请求，restful风格
     *
     * @param pageNum  页码
     * @param pageSize 页面大小
     * @return
     * @see org.springframework.web.bind.annotation.GetMapping
     * @see org.springframework.web.bind.annotation.PathVariable
     * @see org.springframework.web.bind.annotation.GetMapping
     */
    public Result<IPage<E>> page(Integer pageNum,Integer pageSize){
        Page<E> page = new Page<>(pageNum,pageSize);
        return success(mService.page(page));
    }

    /**
     * 通过id查询
     *
     * @param id 主键
     * @return
     * @see org.springframework.web.bind.annotation.GetMapping
     * @see org.springframework.web.bind.annotation.PathVariable
     */
    public Result<E> getById(Long id){
        return success(mService.getById(id));
    }

}
