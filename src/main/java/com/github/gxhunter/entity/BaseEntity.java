package com.github.gxhunter.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 树荫下的天空
 * @date 2019/2/26 10:04
 */
public interface BaseEntity<T extends Serializable>{
    T getId();
    void setId(T id);
}
