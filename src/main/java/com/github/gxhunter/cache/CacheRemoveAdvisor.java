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
import org.springframework.util.CollectionUtils;

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
    private final SpelPaser mSpelPaser = new SpelPaser();
    private final ICacheManager mCacheManager;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Method method = invocation.getMethod();
        ICacheManager cacheManager = mCacheManager;
        CacheRemove cache = method.getAnnotation(CacheRemove.class);
        if (StringUtils.isNotBlank(cache.cacheManager())) {
            cacheManager = SpringUtil.getBean(cache.cacheManager(), ICacheManager.class);
        }
        //        前缀列表
        List<String> prefixList = Arrays.stream(cache.prefix())
                .map(el -> mSpelPaser.parse(el, method, invocation.getArguments(), String.class))
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toList());

        String key = mSpelPaser.parse(cache.key(), method, invocation.getArguments());

        if (StringUtils.isBlank(key) || CollectionUtils.isEmpty(prefixList)) {
            log.warn("你的key/prefix为空,不删除缓存,前缀：{}，key:{}", prefixList, key);
        } else {
            cacheManager.remove(prefixList, key);
        }
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
