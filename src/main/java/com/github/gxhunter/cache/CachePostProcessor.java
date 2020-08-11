package com.github.gxhunter.cache;

import com.github.gxhunter.util.AopUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author 树荫下的天空
 * @date 2020/8/5 21:18
 */

@RequiredArgsConstructor
@Slf4j
public class CachePostProcessor implements BeanPostProcessor, ApplicationContextAware{
    private final CacheContextHolder mCacheContextHolder;
    private ApplicationContext context;

    @Override
    public Object postProcessAfterInitialization(Object bean,String beanName) throws BeansException{
        List<Method> targetMethod = AopUtils.getTargetMethod(Cache.class,bean.getClass(),true);
        if(!CollectionUtils.isEmpty(targetMethod)){
            for(Method method : targetMethod){
                Cache cache = method.getDeclaredAnnotation(Cache.class);
                CacheContext cacheContext = CacheableContext.builder()
                        .cacheManager((ICacheManager) context.getBean(cache.cacheManager()))
                        .cacheAnnotation(cache)
                        .condition(cache.condition())
                        .unless(cache.unless())
                        .key(cache.key())
                        .keyGenerator(context.getBean(cache.keyGenerator()))
                        .timeout(cache.timeout())
                        .build();
                mCacheContextHolder.saveCacheContext(method,AopProxyUtils.ultimateTargetClass(bean),cacheContext);
                log.debug("新增缓存拦截，{}到{}-{}",cache,AopProxyUtils.ultimateTargetClass(bean),method);
            }
        }

        targetMethod = AopUtils.getTargetMethod(CacheRemove.class,bean.getClass(),true);
        if(!CollectionUtils.isEmpty(targetMethod)){
            for(Method method : targetMethod){
                CacheRemove cache = method.getDeclaredAnnotation(CacheRemove.class);
                CacheContext cacheContext = CacheRemoveContext.builder()
                        .cacheManager((ICacheManager) context.getBean(cache.cacheManager()))
                        .cacheAnnotation(cache)
                        .condition(cache.condition())
                        .beforeInvocation(cache.beforeInvocation())
                        .keyGenerator(context.getBean(cache.keyGenerator()))
                        .key(cache.key())
                        .build();
                mCacheContextHolder.saveCacheContext(method,AopProxyUtils.ultimateTargetClass(bean),cacheContext);
                log.debug("新增缓存拦截，{}到{}-{}",cache,AopProxyUtils.ultimateTargetClass(bean),method);
            }
        }

        return bean;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException{
        this.context = applicationContext;
    }
}
