package com.github.gxhunter.cache;

import java.lang.annotation.*;

/**
 * @author wanggx
 * @date 2020-01-09 19:45
 **/
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.METHOD)
@Inherited
public @interface CacheRemove {
    /**
     * 缓存的key，el表达式,多个key根据策略不同，存入的方式也不一样
     * @return
     * @see CacheStrategy
     */
    String[] key();

    /**
     * 多个key时的匹配策略，当前支持and和or
     * @see CacheStrategy
     * @return
     */
    CacheStrategy keyStrategy() default CacheStrategy.AND;

    /**
     * 前缀，el表达式
     * @return
     */
    String prefix() default "''";
}
