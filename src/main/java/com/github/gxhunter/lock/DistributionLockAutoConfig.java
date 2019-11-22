package com.github.gxhunter.lock;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author wanggx
 * @date 2019/5/31 11:33
 */
@Configuration
@ConditionalOnBean(RedisTemplate.class)
@ConditionalOnClass(RedisOperations.class)
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class DistributionLockAutoConfig{
    @Bean
    @ConditionalOnMissingBean
    public LockAdvice lockAdvice(AbstractLockTemplate lockTemplate){
        return new LockAdvice(lockTemplate);
    }

    @Bean
    @ConditionalOnMissingBean
    public AbstractLockTemplate redisLockTemplate(RedisTemplate redisTemplate){
        return new RedisLockTemplate(redisTemplate);
    }

}
