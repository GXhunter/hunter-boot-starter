package com.github.gxhunter.cache;

import lombok.Data;

import java.lang.annotation.Annotation;

/**
 * @author 树荫下的天空
 * @date 2020/8/11 18:06
 */
@Data
public class MethodInfo {
    private ProxyMethodMetadata methodData;

    private CacheContext cacheContext;

    private Annotation annotation;

    private String cacheName;
}
