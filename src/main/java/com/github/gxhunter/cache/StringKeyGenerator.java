package com.github.gxhunter.cache;

import com.github.gxhunter.util.SpelPaser;
import lombok.AllArgsConstructor;

/**
 * @author 树荫下的天空
 * @date 2020/8/10 19:07
 */
@AllArgsConstructor
public class StringKeyGenerator implements KeyGenerator{
    private final SpelPaser mSpelPaser;

    @Override
    public Object generate(String cacheName,ProxyMethodMetadata methodMetadata,CacheContext cacheContext,Object returnValue){
        String key = mSpelPaser.parse(cacheContext.getKey(),methodMetadata.getMethod(),methodMetadata.getArgs(),returnValue,String.class);
        return cacheName+"::"+key;
    }
}
