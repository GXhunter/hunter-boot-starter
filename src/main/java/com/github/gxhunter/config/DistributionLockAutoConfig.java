package com.github.gxhunter.config;

import com.github.gxhunter.lock.AbstractLockTemplate;
import com.github.gxhunter.lock.RedisLockTemplate;
import com.github.gxhunter.lock.LockAdvice;
import com.github.gxhunter.service.IRedisClient;
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
@ConditionalOnBean(IRedisClient.class)
public class DistributionLockAutoConfig{
    @Bean
    @ConditionalOnMissingBean
    public LockAdvice lockAdvice(AbstractLockTemplate lockTemplate){
        return new LockAdvice(lockTemplate);
    }

    @Bean
    @ConditionalOnMissingBean
    public AbstractLockTemplate LockTemplate(RedisTemplate redisTemplate){
        return new RedisLockTemplate(redisTemplate);
    }

}
