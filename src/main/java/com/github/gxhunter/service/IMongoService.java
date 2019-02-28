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
     * @param spit
     */
    E add(E spit);

    E update(Long id,E entity);

    E saveOrUpdate(E entity);

    /**
     * 通过id 删除
     *
     * @param id
     */
    void deleteById(Long id);

    Page<E> page(Pageable page);

    void deleteAll();
}
