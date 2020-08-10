package com.github.gxhunter.cache;

/**
 * @author 树荫下的天空
 * @date 2020/8/10 19:36
 */
public class MethodGenericKeyGenerator implements KeyGenerator{
    @Override
    public Object generate(ProxyMethodMetadata methodMetadata,CacheContext cacheContext,Object returnValue){
        return methodMetadata;
    }
}
