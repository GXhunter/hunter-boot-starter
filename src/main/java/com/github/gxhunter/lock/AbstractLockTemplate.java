package com.github.gxhunter.lock;

import com.github.gxhunter.util.IdWorker;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 分布式锁
 *
 * @author hunter
 */
@Slf4j
public abstract class AbstractLockTemplate {

    /**
     * 加锁,同时设置锁超时时间
     *
     * @param key        分布式锁的key
     * @param value      分布式锁的value
     * @param expireTime 单位是ms
     * @return 生成的锁名称，null表示上锁失败
     */
    public abstract String lock(String key, String value, long expireTime);


    /**
     * 解锁
     *
     * @param key   锁
     * @param value 分布式锁的value
     * @return 是否释放成功
     */
    public abstract boolean unlock(String key, String value);

    /**
     * 分布式锁的value
     * @return 雪花算法-线程id
     */
    public String getLockValue() {
        return IdWorker.getId() + "-" + Thread.currentThread().getId();
    }


}
