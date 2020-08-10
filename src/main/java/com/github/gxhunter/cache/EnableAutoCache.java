package com.github.gxhunter.cache;

import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;

import java.lang.annotation.*;

/**
 * 添加此注解支持缓存
 * @author wanggx
 * @date 2020-01-09 18:51
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import(CacheAutoConfig.class)
public @interface EnableAutoCache {
    int order() default Ordered.LOWEST_PRECEDENCE;
}
