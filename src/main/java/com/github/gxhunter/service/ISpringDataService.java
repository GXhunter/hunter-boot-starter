package com.github.gxhunter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * @param <E> 实体类
 * @author 树荫下的天空
 */
public interface ISpringDataService<E>{
    /**
     * 查询所有
     *
     * @return
     */
    List<E> list();

    /**
     * 通过id查找
     *
     * @param id
     * @return
     */
    E getById(Long id);

    /**
     * 新增
     *
     * @param spit
     */
    E save(E spit);

    /**
     * 批量保存
     * @param entities
     * @return
     */
    Iterable<E> saveBatch(Iterable<E> entities);

    /**
     * 通过id修改
     * @param id
     * @param entity
     * @return
     */
    E updateById(Long id,E entity);

    /**
     * 新增或修改
     * @param entity 主键为null时新增，否则修改
     * @return
     */
    E saveOrUpdate(E entity);

    /**
     * 通过id 删除
     *
     * @param id
     */
    void removeById(Long id);

    Page<E> page(Pageable page);

    /**
     * 分页查找
     *
     * @param pageNum  页码（从0开始）
     * @param pageSize 页面大小
     * @return
     */
    Page<E> page(Integer pageNum,Integer pageSize);

    /**
     * 删除所有
     */
    void removeAll();

    void removeAll(Iterable<E> iterable);

    /**
     * 是否存在
     *
     * @param id 主键
     * @return
     */
    boolean existsById(Long id);

    /**
     * 返回可用实体的数量
     * @return 实体数量
     */
    long count();

}
