package com.github.gxhunter.lock;

import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

/**
 * 分布式锁
 * @author hunter
 */
@Slf4j
public abstract class AbstractLockTemplate{
    /**
     * 可重入时，实例的唯一标识，与线程id构成redis-value
     */
    private static final String UUID_PREFIX = UUID.randomUUID().toString();

    /**
     * 加锁,同时设置锁超时时间
     *
     * @param key        分布式锁的key
     * @param value      分布式锁的value
     * @param expireTime 单位是ms
     * @return 生成的锁名称，null表示上锁失败
     */
    public abstract String lock(String key,String value,long expireTime);


    /**
     * 解锁
     *
     * @param key   锁
     * @param value 分布式锁的value
     * @return 是否释放成功
     */
    public abstract boolean unlock(String key,String value);

    /**
     * 分布式锁的value
     *
     * @param reentrant 是否可重入
     * @return 非重入锁时，每次返回的都不一样。可重入锁，相同应用实例的相同线程返回内容一致。
     */
    public String getLockValue(boolean reentrant){
        return reentrant ? UUID_PREFIX : UUID.randomUUID().toString() + Thread.currentThread().getId();
    }


}
