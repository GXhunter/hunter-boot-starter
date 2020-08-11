package com.github.gxhunter.cache;

import com.github.gxhunter.util.ConstantValue;
import com.github.gxhunter.util.SpelPaser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

/**
 * 缓存注解支持
 *
 * @author wanggx
 * @date 2020-01-03 19:06
 **/
@Slf4j
@AllArgsConstructor
public class CacheInterceptor extends AbstractPointcutAdvisor implements MethodInterceptor, ConstantValue.Cache{
    private final SpelPaser mSpelPaser = new SpelPaser();
    private final CacheContextHolder mCacheContextHolder;
    private final int order;

    /**
     * @param invocation
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable{
        Method method = invocation.getMethod();
        ProxyMethodMetadata methodContext = ProxyMethodMetadata.builder().method(method).targetClass(invocation.getThis()).args(invocation.getArguments()).build();
        CacheRemoveContext cacheRemoveContext = (CacheRemoveContext) mCacheContextHolder.getCacheOperations(method,invocation.getThis().getClass(),CacheRemove.class);

        processCacheRemove(cacheRemoveContext,methodContext,true,null);

        CacheableContext cacheContext = (CacheableContext) mCacheContextHolder.getCacheOperations(method,invocation.getThis().getClass(),Cache.class);

        Object cacheValue = findCachedItem(cacheContext,methodContext);
        if(cacheValue != null){
            return cacheValue;
        }
        Object returnValue = invocation.proceed();

        cachePut(returnValue,cacheContext,methodContext);

        processCacheRemove(cacheRemoveContext,methodContext,false,returnValue);
        return returnValue;
    }


    private void cachePut(Object returnValue,CacheableContext cacheContext,ProxyMethodMetadata methodContext){
        if(cacheContext == null){
            return;
        }
        Cache cache = cacheContext.getCacheAnnotation();
        boolean unless = mSpelPaser.parse(cache.unless(),methodContext.getMethod(),methodContext.getArgs(),returnValue,boolean.class);
        if(!unless){
            for(String cacheName : cache.cacheNames()){
                Object keyList = cacheContext.getKeyGenerator().generate(cacheName,methodContext,cacheContext,returnValue);
                log.debug("put缓存,key:{},value:{},超时:{},执行方法:{}",keyList,returnValue,cacheContext.getTimeout(),methodContext);
                cacheContext.getCacheManager().put(keyList,returnValue,cacheContext.getTimeout());
            }
        }
    }

    private void processCacheRemove(CacheRemoveContext cacheContext,ProxyMethodMetadata methodContext,boolean beforeInvocation,Object returnValue){
        CacheRemove cacheRemove = Optional.ofNullable(cacheContext)
                .map(CacheContext::getCacheAnnotation).orElse(null);

        if(cacheRemove == null || cacheRemove.beforeInvocation() != beforeInvocation){
            return;
        }
        boolean condition = mSpelPaser.parse(cacheRemove.condition(),methodContext.getMethod(),methodContext.getArgs(),returnValue,boolean.class);
        if(!condition){
            return;
        }
        for(String cacheName : cacheRemove.cacheNames()){
            Object keyList = cacheContext.getKeyGenerator().generate(cacheName,methodContext,cacheContext,null);
            cacheContext.getCacheManager().remove(keyList);
            log.debug("移除了缓存:{},执行方法:{}",keyList,methodContext);
        }
    }


    /**
     * 获取缓存中的值
     *
     * @param cacheContext  缓存上下文
     * @param methodContext
     * @return
     */
    private Object findCachedItem(CacheContext cacheContext,ProxyMethodMetadata methodContext){
        if(cacheContext == null){
            return null;
        }
        Cache cache = methodContext.getMethod().getAnnotation(Cache.class);
        if(cache == null){
            return null;
        }
        boolean condition = mSpelPaser.parse(cache.condition(),methodContext.getMethod(),methodContext.getArgs(),boolean.class);
        if(!condition){
            return null;
        }
        for(String cacheName : cache.cacheNames()){
            Object cacheKeys = cacheContext.getKeyGenerator().generate(cacheName,methodContext,cacheContext,null);
            if(cacheKeys == null){
                return null;
            }
            Object cacheValue = cacheContext.getCacheManager().get(cacheKeys);
            log.debug("从缓存获取的值为:{},key:{},结果:{},方法是:{}",cacheValue,cacheKeys,cacheValue,methodContext);
            if(cacheValue != null && !CACHE_EMPTY_VALUE.equals(cacheValue)){
                return cacheValue;
            }
        }
        return null;
    }

    @Override
    public Pointcut getPointcut(){
        Pointcut cachePoint = AnnotationMatchingPointcut.forMethodAnnotation(Cache.class);
        Pointcut cacheRemovePoint = AnnotationMatchingPointcut.forMethodAnnotation(CacheRemove.class);
        return new ComposablePointcut(cachePoint).union(cacheRemovePoint);
    }

    @Override
    public int getOrder(){
        return order;
    }

    @Override
    public Advice getAdvice(){
        return this;
    }
}
