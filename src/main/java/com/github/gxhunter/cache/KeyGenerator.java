package com.github.gxhunter.cache;

/**
 * @author 树荫下的天空
 * @date 2020/8/10 19:06
 */
public interface KeyGenerator{

    /**
     * @param cacheName 缓存名称
     * @param methodMetadata 被缓存的方法上下文
     * @param cacheContext 缓存信息
     * @param returnValue 返回值（如果有的话）
     * @return
     */
    Object generate(String cacheName,ProxyMethodMetadata methodMetadata,CacheContext cacheContext,Object returnValue);

}
