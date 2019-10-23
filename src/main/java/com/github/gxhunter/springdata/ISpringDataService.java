package com.github.gxhunter.springdata;

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
     * @param spit 实体类
     * @return 保存后对象
     */
    E save(E spit);

    /**
     * 批量保存
     *
     * @param entities 实体类迭代器
     * @return 实体类迭代器
     */
    Iterable<E> saveBatch(Iterable<E> entities);

    /**
     * 通过id修改
     *
     * @param id     主键
     * @param entity 实体类
     * @return 修改后对象
     */
    E updateById(Long id,E entity);

    /**
     * 新增或修改
     *
     * @param entity 主键为null时新增，否则修改
     * @return 修改后对象
     */
    E saveOrUpdate(E entity);

    /**
     * 通过id 删除
     *
     * @param id id
     */
    void removeById(Long id);

    /**
     * 分页查询
     * @param page 分页对象
     * @return 分页对象
     */
    Page<E> page(Pageable page);

    /**
     * 分页查找
     *
     * @param pageNum  页码（从0开始）
     * @param pageSize 页面大小
     * @return 分页对象
     */
    Page<E> page(Integer pageNum,Integer pageSize);

    /**
     * 删除所有
     */
    void removeAll();

    /**
     * 移除所有
     *
     * @param iterable 迭代器
     */
    void removeAll(Iterable<E> iterable);

    /**
     * 是否存在
     *
     * @param id 主键
     * @return 是否存在
     */
    boolean existsById(Long id);

    /**
     * 返回可用实体的数量
     *
     * @return 实体数量
     */
    long count();

}
