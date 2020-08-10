package com.github.gxhunter.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 树荫下的天空
 * @date 2020/8/5 21:48
 */
@Slf4j
public class CacheContextHolder{
    /**
     * 方法 -> 缓存
     */
    private final Map<MethodKey, CacheContext> attributeCache = new ConcurrentHashMap<>(1024);



    CacheContext getCacheOperations(Method method,@Nullable Class<?> targetClass,Class<? extends Annotation> annotation){
        if(method.getDeclaringClass() == Object.class){
            return null;
        }
        MethodKey cacheKey = new MethodKey(method,targetClass,annotation);
        return this.attributeCache.get(cacheKey);
    }


    void saveCacheContext(Method method,@Nullable Class<?> targetClass,CacheContext cacheMetadata){
        if(method.getDeclaringClass() == Object.class){
            return ;
        }
        MethodKey cacheKey = new MethodKey(method,targetClass,cacheMetadata.getAnnotationType());
        this.attributeCache.put(cacheKey,cacheMetadata);

    }




}
