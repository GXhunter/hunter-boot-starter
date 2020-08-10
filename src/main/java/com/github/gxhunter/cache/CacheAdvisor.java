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
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 缓存注解支持
 *
 * @author wanggx
 * @date 2020-01-03 19:06
 **/
@Slf4j
@AllArgsConstructor
public class CacheAdvisor extends AbstractPointcutAdvisor implements MethodInterceptor, ConstantValue.Cache{
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
            List<String> keyList = generateKey(cacheContext,methodContext,returnValue);
            log.debug("put缓存,key:{},value:{},超时:{},执行方法:{}",keyList,returnValue,cacheContext.getTimeout(),methodContext);
            cacheContext.getCacheManager().put(keyList,returnValue,cacheContext.getTimeout());
        }
    }

    private void processCacheRemove(CacheRemoveContext cacheContext,ProxyMethodMetadata methodContext,boolean beforeInvocation,Object returnValue){
        CacheRemove cacheRemove = Optional.ofNullable(cacheContext)
                .map(CacheContext::getCacheAnnotation).orElse(null);

        if(cacheRemove == null || cacheRemove.beforeInvocation() != beforeInvocation){
            return;
        }
        boolean condition = mSpelPaser.parse(cacheRemove.condition(),methodContext.getMethod(),methodContext.getArgs(),boolean.class);
        if(!condition){
            return;
        }

        List<String> keyList = generateKey(cacheContext,methodContext);
        Long count = cacheContext.getCacheManager().remove(keyList);
        log.debug("移除了缓存:{},影响数量:{},执行方法:{}",keyList,count,methodContext);
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
        List<String> cacheKeys = generateKey(cacheContext,methodContext);
        if(CollectionUtils.isEmpty(cacheKeys)){
            return null;
        }
        Object cacheValue = cacheContext.getCacheManager().get(cacheKeys,methodContext.getMethod().getGenericReturnType());
        log.debug("从缓存获取的值为:{},key:{},方法是:{}",cacheValue,cacheKeys,methodContext);
        return cacheValue;
    }

    @Override
    public Pointcut getPointcut(){
        Pointcut cachePoint = AnnotationMatchingPointcut.forMethodAnnotation(Cache.class);
        Pointcut cacheRemovePoint = AnnotationMatchingPointcut.forMethodAnnotation(CacheRemove.class);
        return new ComposablePointcut(cachePoint).union(cacheRemovePoint);
    }

    List<String> generateKey(CacheContext cacheContext,ProxyMethodMetadata methodMetadata){
        return generateKey(cacheContext,methodMetadata,null);
    }
    /**
     * 生成目标key
     *
     * @param cacheContext   缓存上下文
     * @param methodMetadata 方法元数据
     * @return 多个key表示缓存多处
     */
    List<String> generateKey(CacheContext cacheContext,ProxyMethodMetadata methodMetadata,Object returnValue){
        List<String> cacheNames = mSpelPaser.parse(cacheContext.getPrefix(),methodMetadata.getMethod(),methodMetadata.getArgs(),List.class);
        String keys = mSpelPaser.parse(cacheContext.getKey(),methodMetadata.getMethod(),methodMetadata.getArgs(),returnValue,String.class);
        return cacheNames.stream().filter(Objects::nonNull).map(p -> p + "::" + keys).collect(Collectors.toList());
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
