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

    /**
     * 要锁定的key中包含的属性
     */
    String[] keys() default {};

    /**
     * 超时时间，单位毫秒
     */
    long expireTime() default 10000;

    /**
     * 获取不到锁的等待时间，单位毫秒
     */
    long retryTimes() default 2000;

}
