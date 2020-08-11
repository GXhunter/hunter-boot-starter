package com.github.gxhunter.cache;

import lombok.Builder;
import lombok.Getter;

/**
 * @author 树荫下的天空
 * @date 2020/8/10 10:20
 */
@Getter
public class CacheableContext extends CacheContext<Cache>{

    /**
     * 超时时间
     */

    private final long timeout;
    /**
     * 是否缓存结果
     */
    private final String unless;

    @Builder
    public CacheableContext(String key,KeyGenerator keyGenerator,String cacheResolver,String condition,String unless,ICacheManager cacheManager,Cache cacheAnnotation,long timeout){
        super(key,cacheResolver,condition,cacheManager,keyGenerator,cacheAnnotation,Cache.class);
        this.timeout = timeout;
        this.unless = unless;
    }
}
