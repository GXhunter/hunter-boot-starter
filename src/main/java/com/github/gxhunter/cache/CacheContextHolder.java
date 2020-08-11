package com.github.gxhunter.cache;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
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
    private final Map<MethodKey, List<CacheContext>> attributeCache = new ConcurrentHashMap<>(1024);



    List getCacheOperations(Method method,@Nullable Class<?> targetClass,Class<? extends Annotation> annotation){
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
        List<CacheContext> list = this.attributeCache.get(cacheKey);
        if(list != null){
            list.add(cacheMetadata);
        }else {
            attributeCache.put(cacheKey,Arrays.asList(cacheMetadata));
        }

    }




}
