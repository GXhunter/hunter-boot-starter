package com.github.gxhunter.cache;

/**
 * @author 树荫下的天空
 * @date 2020/8/10 19:06
 */
public interface KeyGenerator{

    Object generate(ProxyMethodMetadata methodMetadata,CacheContext cacheContext,Object returnValue);

    default Object generate(ProxyMethodMetadata methodMetadata,CacheContext cacheContext){
        return generate(methodMetadata,cacheContext,null);
    }



}
