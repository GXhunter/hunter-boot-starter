package com.github.gxhunter.anno;

import com.github.gxhunter.lock.RedisLockTemplate;

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
public @interface Lock{

    /**
     * <p>
     *
     *  要锁定的key中包含的属性,不指定时锁定当前方法
     *  指定key时将覆盖默认key生成策略，
     *  固定前缀：{keyPrex}{split}
     *  默认的key策略：固定前缀+方法全路径
     *  自行指定key时：固定前缀+指定的key(多个key使用分隔符分割，且每个key支持spel语法)
     * </p>
     *
     * @see RedisLockTemplate#keyPrex
     * @see RedisLockTemplate#SPLIT
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

    /**
     * 方法执行后自动释放锁，设置为false时，只能等待超时释放
     * @return
     */
    boolean autoRelease() default true;

}
