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
     * 要锁定的key中包含的属性,不指定时锁定当前方法
     */
    String[] keys() default {};

    /**
     * 超时时间，单位毫秒
     * 默认30秒
     */
    long expireTime() default 30*1000;

    /**
     * 获取不到锁的等待时间，单位毫秒
     * 默认10秒
     */
    long retryTimes() default 10*1000;

}
