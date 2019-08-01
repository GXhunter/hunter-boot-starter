package com.github.gxhunter.entity;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;

/**
 * @author 树荫下的天空
 * @date 2019/2/28 9:03
 */
@Data
@ToString
public abstract class BaseIdEntity{
    /**
     * 主键
     */
    @Id
    private Long id;
}
