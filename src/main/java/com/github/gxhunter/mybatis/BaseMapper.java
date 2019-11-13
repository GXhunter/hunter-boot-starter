package com.github.gxhunter.mybatis;

import com.github.gxhunter.entity.BaseIdEntity;

import java.io.Serializable;
import java.util.List;

/**
 * 基本mapper
 * @author hunter
 */
public interface BaseMapper<T extends BaseIdEntity>{
    /**
     * 通过id查找
     *
     * @param id 主键
     * @return 数据
     */
    T selectById(Serializable id);

    /**
     * 通过id删除
     *
     * @param id 主键
     * @return 是否删除成功
     */
    boolean deleteById(Serializable id);

    /**
     * 查询所有
     *
     * @return 返回数据
     */
    List<T> selectList();

    /**
     * 通过id修改
     *
     * @param entity 实体类
     * @return 是否成功
     */
    boolean updateById(T entity);

    /**
     * 通过id批量修改
     *
     * @param entityList 实体类
     */
    void updateBatchById(List<T> entityList);

    /**
     * 新增一条数据
     *
     * @param entity 实体类
     * @return 是否成功
     */
    boolean insert(T entity);

    /**
     * 批量新增数据
     *
     * @param entityList 实体类
     */
    void batchInsert(List<T> entityList);


}
