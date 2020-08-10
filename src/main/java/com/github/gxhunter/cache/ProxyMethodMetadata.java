package com.github.gxhunter.cache;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;

/**
 * @author 树荫下的天空
 * @date 2020/8/10 10:28
 * 代理目标方法元数据
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProxyMethodMetadata{

    /**
     * 方法
     */
    private Method method;

    /**
     * 目标对象（非代理）
     */
    private Object targetClass;

    /**
     * 方法实际参数
     */
    private Object[] args;
}
