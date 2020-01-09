package com.github.gxhunter.cache;

import com.github.gxhunter.util.SpelPaser;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.ApplicationContext;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.stereotype.Component;

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
@Aspect
@Slf4j
@AllArgsConstructor
public class CacheAdvisor {
    private final ApplicationContext mContext;
    private final SpelPaser mSpelPaser = new SpelPaser();
    private final DefaultParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();
    @SneakyThrows
    @Around("@annotation(com.github.gxhunter.cache.Cache)")
    public Object cache(ProceedingJoinPoint pjp) {
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        MethodBasedEvaluationContext context
                = new MethodBasedEvaluationContext(null, method, pjp.getArgs(), parameterNameDiscoverer);

        Cache cache = method.getAnnotation(Cache.class);
        AbstractCacheTemplate cacheTemplate = mContext.getBean(cache.keyStrategy().getCacheTemplate());

        List<String> keys = Arrays.stream(cache.key())
                .map(el->mSpelPaser.parse(el,method,pjp.getArgs(),String.class))
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toList());
        Object result;
        result = cacheTemplate.get(cache.prefix(), keys, method.getGenericReturnType());

        if (result == null) {
            result = pjp.proceed();
            cacheTemplate.put(cache.prefix(), keys, result, cache.timeout());
        }
        return result;
    }

}
