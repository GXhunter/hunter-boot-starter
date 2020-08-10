package com.github.gxhunter.cache;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.annotation.Annotation;

/**
 * @author 树荫下的天空
 * @date 2020/8/5 21:58
 * 缓存上下文
 */
@Data
@AllArgsConstructor
public class CacheContext<T extends Annotation>{

    /**
     * 缓存名，多个表示多处缓存
     */
    private final String prefix;

    /**
     * key，和{{@link #prefix}}通过"::"拼接
     */
    private final String key;

    private final String cacheResolver;

    /**
     * 是否进入缓存
     */
    private final String condition;


    /**
     * 缓存管理器
     */
    private final ICacheManager cacheManager;

    /**
     * 注解
     */
    private final T cacheAnnotation;

    private final Class<T> annotationType;

    public T getCacheAnnotation(){
        return cacheAnnotation;
    }


}
