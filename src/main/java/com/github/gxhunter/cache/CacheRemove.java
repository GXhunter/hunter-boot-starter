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
     */
    String key();


    /**
     * 前缀，el表达式
     * @return
     */
    String[] prefix();

    /**
     * @return 缓存管理器
     */
    String cacheManager() default "redisCacheManager";
}
