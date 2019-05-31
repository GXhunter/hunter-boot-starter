package com.github.gxhunter.entity;

import java.io.Serializable;

/**
 * @author 树荫下的天空
 * @date 2019/2/26 10:04
 */
public interface BaseEntity<T extends Serializable>{
    /**
     * 获取主键
     * @return
     */
    T getId();

    /**
     * 设置主键
     * @param id
     */
    void setId(T id);
}
