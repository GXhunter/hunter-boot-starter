package com.github.gxhunter.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author wanggx
 * @date 2019/5/24 17:02
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RedisLock{
    String keyPrefix() default "";

    /**
     * 要锁定的key中包含的属性
     */
    String[] keys() default {};

    /**
     * 是否阻塞锁；
     * 1. true：获取不到锁，阻塞一定时间；
     * 2. false：获取不到锁，立即返回
     */
    boolean isSpin() default true;

    /**
     * 超时时间，单位秒
     */
    int expireTime() default 10000;

    /**
     * 等待时间，单位秒
     */
    int waitTime() default 50;

    /**
     * 获取不到锁的等待时间，单位秒
     */
    int retryTimes() default 20;

}
