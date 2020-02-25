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
public class CacheAdvisor extends AbstractPointcutAdvisor implements MethodInterceptor, ConstantValue.Cache {
    private final SpelPaser mSpelPaser = new SpelPaser();
    private final ICacheManager mCacheManager;

    /**
     * @param invocation
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Method method = invocation.getMethod();
        Cache cache = method.getAnnotation(Cache.class);

//        前缀列表
        List<String> prefixList = Arrays.stream(cache.prefix())
                .map(el->mSpelPaser.parse(el, invocation, String.class))
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toList());

        String key = mSpelPaser.parse(cache.key(), method, invocation.getArguments());

        if (StringUtils.isBlank(key) || CollectionUtils.isEmpty(prefixList)) {
            log.debug("key/prefix为空,不走缓存,前缀：{}，key:{}", prefixList, key);
            return invocation.proceed();
        }

        Object result = mCacheManager.get(prefixList, key, method.getGenericReturnType());
        if (CACHE_EMPTY_VALUE.equals(result)) {
            return null;
        }
        if (result == null) {
//            缓存获取不到数据，执行目标方法，并缓存
            result = invocation.proceed();
            mCacheManager.put(prefixList, key, result, cache.timeout());
        }

        return result;
    }

    @Override
    public Pointcut getPointcut() {
        return AnnotationMatchingPointcut.forMethodAnnotation(Cache.class);
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
