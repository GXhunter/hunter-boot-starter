package com.github.gxhunter.lock;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.Collections;

/**
 * redis分布式锁
 *
 * @author wanggx
 * @date 2019/5/24 17:17
 */
@Slf4j
public class RedisLockTemplate extends AbstractLockTemplate{

    /**
     * redis操作客户端
     */
    private RedisTemplate<String, Object> mRedisTemplate;
    /**
     * 加锁LUA脚本
     */
    private static final RedisScript<String> SCRIPT_LOCK = new DefaultRedisScript<>("return redis.call('set',KEYS[1],ARGV[1],'NX','PX',ARGV[2])",String.class);
    /**
     * 释放锁LUA脚本
     */
    private static final RedisScript<String> SCRIPT_UNLOCK = new DefaultRedisScript<>("if redis.call('get',KEYS[1]) == ARGV[1] then return tostring(redis.call('del', KEYS[1])==1) else return 'false' end",String.class);
    private static final String LOCK_SUCCESS = "OK";


    public RedisLockTemplate(RedisTemplate<String, Object> redisTemplate){
        mRedisTemplate = redisTemplate;
    }


    /**
     * 加锁,同时设置锁超时时间
     *
     * @param key        分布式锁的key
     * @param expireTime 单位是ms
     * @return 生成的锁名称,null
     */
    public String lock(String key,String value,long expireTime){
//        只有当前应用，当前线程才能解锁
        Object lockResult = mRedisTemplate.execute(
                SCRIPT_LOCK,
                mRedisTemplate.getStringSerializer(),
                mRedisTemplate.getStringSerializer(),
                Collections.singletonList(key),
                value,
                String.valueOf(expireTime)
        );
        return LOCK_SUCCESS.equals(lockResult) ? value : null;
    }


    /**
     * 解锁
     *
     * @param key 锁
     * @return 是否释放成功
     */
    public boolean unlock(String key,String value){
        log.debug("redis unlock debug, start. resource:[{}].",key);
        String result = mRedisTemplate.execute(
                SCRIPT_UNLOCK,
                mRedisTemplate.getStringSerializer(),
                mRedisTemplate.getStringSerializer(),
                Collections.singletonList(key),value
        );
        log.debug("redis lock release status {}",result);
        return Boolean.parseBoolean(result);
    }
}
