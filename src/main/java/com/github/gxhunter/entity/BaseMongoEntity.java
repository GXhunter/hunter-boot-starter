package com.github.gxhunter.entity;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

/**
 * @author 树荫下的天空
 * @date 2019/2/28 9:03
 */
@Data
@ToString
public abstract class BaseMongoEntity implements Serializable{
    /**
     * mongodb主键
     */
    @Id
    private Long id;
}
