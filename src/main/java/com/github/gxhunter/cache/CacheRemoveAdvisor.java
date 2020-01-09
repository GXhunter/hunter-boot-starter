package com.github.gxhunter.cache;

import com.github.gxhunter.util.ConstantValue;
import com.github.gxhunter.util.SpelPaser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 缓存注解支持
 *
 * @author wanggx
 * @date 2020-01-03 19:06
 **/
@Slf4j
@AllArgsConstructor
public class CacheRemoveAdvisor extends AbstractPointcutAdvisor implements MethodInterceptor, ConstantValue.Cache {
    private final ApplicationContext mContext;
    private final SpelPaser mSpelPaser = new SpelPaser();

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Method method = invocation.getMethod();
        CacheRemove cache = method.getAnnotation(CacheRemove.class);
        AbstractCacheTemplate cacheTemplate = mContext.getBean(cache.keyStrategy().getCacheTemplate());
        List<String> keys = Arrays.stream(cache.key())
                .filter(StringUtils::isNotBlank)
                .map(el->mSpelPaser.parse(el,method,invocation.getArguments(),String.class))
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toList());
        String prefix = mSpelPaser.parse(cache.prefix(), method, invocation.getArguments(), String.class);
        cacheTemplate.remove(prefix,keys);
        return invocation.proceed();
    }

    @Override
    public Pointcut getPointcut() {
        return AnnotationMatchingPointcut.forMethodAnnotation(CacheRemove.class);
    }

    @Override
    public int getOrder() {
        return AOP_ORDER;
    }

    @Override
    public Advice getAdvice() {
        return this;
    }
}
