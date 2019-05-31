package com.github.gxhunter.service;

import com.github.gxhunter.entity.BaseMongoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @param <E>  实体类
 * @author 树荫下的天空
 */
public interface IMongoService<E extends BaseMongoEntity>{
    /**
     * 查询所有
     *
     * @return
     */
    List<E> findAll();

    /**
     * 通过id查找
     *
     * @param id
     * @return
     */
    E findById(Long id);

    /**
     * 新增
     *
     * @param spit 实体类
     * @return
     */
    E add(E spit);

    /**
     * 修改
     * @param id  主键
     * @param entity 实体类
     * @return
     */
    E update(Long id,E entity);

    /**
     * 修改或删除
     * @param entity
     * @return
     */
    E saveOrUpdate(E entity);

    /**
     * 通过id 删除
     *
     * @param id
     */
    void deleteById(Long id);

    /**
     * 分页查询
     * @param page
     * @return
     */
    Page<E> page(Pageable page);

    /**
     * 删除所有
     */
    void deleteAll();
}
