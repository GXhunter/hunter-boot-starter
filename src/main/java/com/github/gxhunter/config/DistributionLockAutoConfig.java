package com.github.gxhunter.config;

import com.github.gxhunter.lock.RedisDistributionLock;
import com.github.gxhunter.lock.RedisLockAdvice;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author wanggx
 * @date 2019/5/31 11:33
 */
@Configuration
public class DistributionLockAutoConfig{
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(RedisDistributionLock.class)
    public RedisLockAdvice mRedisLockAdvice(RedisDistributionLock redisDistributionLock){
        return new RedisLockAdvice(redisDistributionLock);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(RedisTemplate.class)
    public RedisDistributionLock mRedisDistributionLock(RedisTemplate redisTemplate){
        return new RedisDistributionLock(redisTemplate);
    }

}
