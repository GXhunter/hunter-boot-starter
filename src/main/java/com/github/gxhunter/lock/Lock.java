package com.github.gxhunter.lock;

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
     * 同一个应用，同一个线程可重入。
     * 要锁定的key中包含的属性,不指定时锁定当前方法
     * 指定key时将覆盖默认key生成策略，
     * 支持spel表达式，也支持纯文本字符串
     * 固定前缀：{keyPrex}{split}
     * 默认的key策略：固定前缀+方法全路径
     * 自行指定key时：固定前缀+指定的key(多个key使用分隔符分割，且每个key支持spel语法)
     * </p>
     *
     * @see AbstractLockTemplate#keyPrex
     * @see AbstractLockTemplate#SPLIT
     */
    String[] keys() default {};

    /**
     * 超时时间，单位毫秒
     * 默认30秒
     */
    long expireTime() default 30 * 1000;

    /**
     * 获取不到锁的等待时间，单位毫秒
     * 默认10秒
     */
    long retryTimes() default 10 * 1000;

    /**
     * 是否延迟释放
     *
     * @return false:不延迟，方法执行结束就释放。true:方法执行结束后不释放，等待超时
     */
    boolean delay() default false;

    /**
     * 是否可重入（同个应用同个线程）
     * @return
     */
    boolean reentrant() default true;

}
