package com.github.gxhunter.cache;

import com.github.benmanes.caffeine.cache.Cache;
import lombok.AllArgsConstructor;

/**
 * @author 树荫下的天空
 * @date 2020/8/11 18:00
 */
@AllArgsConstructor
public class CaffeineCacheManagerAdapting<K,V> implements ICacheManager{
    private final Cache<K,V> mCache;
    @Override
    public void put(Object key,Object object,long timeout){
        mCache.put((K)key,(V)object);
    }

    @Override
    public void remove(Object keys){
        mCache.invalidate(keys);
    }

    @Override
    public Object get(Object key){
        return mCache.getIfPresent(key);
    }
}
