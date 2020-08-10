package com.github.gxhunter.cache;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 缓存 {class}-{method}-{annotation}  =>  {@link CacheContext}
 * @author 树荫下的天空
 * @date 2020/8/5 21:52
 */
@AllArgsConstructor
@EqualsAndHashCode
public class MethodKey{
    /**
     * 被拦截的方法
     */
    private final Method method;
    /**
     * 被拦截的对象
     */
    private final Class<?> targetClass;
    /**
     * 生效注解
     */
    private final Class<? extends Annotation> annotation;

}
