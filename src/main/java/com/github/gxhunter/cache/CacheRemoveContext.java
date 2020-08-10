package com.github.gxhunter.cache;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * @author 树荫下的天空
 * @date 2020/8/10 10:20
 */
@EqualsAndHashCode(callSuper = true)
public class CacheRemoveContext extends CacheContext<CacheRemove>{
    @Getter
    private final boolean beforeInvocation;

    @Builder
    public CacheRemoveContext(boolean beforeInvocation,String prefix,String key,String cacheResolver,String condition,ICacheManager cacheManager,CacheRemove cacheAnnotation){
        super(prefix,key,cacheResolver,condition,cacheManager,cacheAnnotation,CacheRemove.class);
        this.beforeInvocation = beforeInvocation;
    }
}
