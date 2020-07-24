package com.github.gxhunter.cache;


import java.lang.annotation.*;

/**
 * @author wanggx
 * @date 2020-01-03 19:07
 **/
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.METHOD)
@Inherited
public @interface Cache {
    /**
     * 缓存的key，el表达式,多个key根据策略不同，存入的方式也不一样
     * @return
     */
    String key();

    /**
     * 超时，单位秒
     * @return
     */
    long timeout() default 60L;

    /**
     * 前缀，el表达式。几个前缀存储几个缓存。
     * @return 默认为方法签名
     */
    String[] prefix() ;

    /**
     * 条件满足时才缓存
     * @return
     */
    String condition() default "true";

    /**
     * 缓存管理器
     * @return 缓存管理器
     */
    String cacheManager() default "redisCacheManager";

    /**
     * 不缓存的情况
     * @return true：不缓存，false：需要缓存
     */
    String unless() default "false";
}
