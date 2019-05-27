package com.github.gxhunter.job;

import com.github.gxhunter.service.IRedisClient;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author wanggx
 * @date 2019/5/24 17:17
 */
@Slf4j
public class RedisDistributionLock{

    @Autowired
    IRedisClient<String,String> mRedisClient;

    private static final Logger logger = LoggerFactory.getLogger(RedisDistributionLock.class);

    //key的TTL,一天 单位秒
    private static final int FINAL_DEFAULTTT_LWITH_KEY = 24 * 60 * 60;

    //锁默认超时时间,20秒
    private static final long DEFAULT_EXPIRE_TIME = 20 * 1000;

    /**
     * 统一用户标识，没有指定client时，默认为此值，加锁和解锁无须同一个客户端
     */
    private static final String DEFAULT_REQUEST_ID = UUID.randomUUID().toString();

    /**
     * 加锁
     *
     * @param key
     * @param expireTime
     * @return
     */
    public Boolean lock(String key,long expireTime){
        return lock(key,DEFAULT_REQUEST_ID,expireTime);
    }

    /**
     * 加锁,同时设置锁超时时间
     *
     * @param key        分布式锁的key
     * @param requestId  请求标识
     * @param expireTime 单位是ms
     * @return 是否成功
     */
    public Boolean lock(String key,String requestId,long expireTime){
        return mRedisClient.set(key,requestId,expireTime,TimeUnit.SECONDS,RedisStringCommands.SetOption.SET_IF_ABSENT);
    }


    /**
     * 释放锁
     *
     * @param key
     * @return
     */
    public boolean unlock(String key){
        return unlock(key,DEFAULT_REQUEST_ID);
    }

    /**
     * 解锁
     *
     * @param key       锁
     * @param requestId 请求标识
     * @return 是否释放成功
     */
    public boolean unlock(String key,String requestId){
        logger.debug("redis unlock debug, start. resource:[{}].",key);
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(script);
        Long result = mRedisClient.execute(redisScript,Collections.singletonList(key),Collections.singletonList(requestId));
        return result == 1L;
    }
}
