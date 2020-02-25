package com.github.gxhunter.cache;

import com.github.gxhunter.util.BeanMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author wanggx
 * @date 2020-01-09 18:56
 **/
@Configuration
@Slf4j
public class CacheAutoConfig {

    @Bean
    @ConditionalOnMissingBean(ICacheManager.class)
    @ConditionalOnClass(RedisTemplate.class)
    public ICacheManager redisCacheManager(RedisTemplate<String, String> redisTemplate, BeanMapper jsonMapper) {
        log.debug("当前使用默认RedisTemplate作为缓存工具");
        return new RedisCacheManager(redisTemplate, jsonMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(ICacheManager.class)
    public CacheAdvisor cacheAdvisor(ICacheManager cacheManager) {
        log.debug("自动缓存注解已开启支持");
        return new CacheAdvisor(cacheManager);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(ICacheManager.class)
    public CacheRemoveAdvisor cacheRemoveAdvisor(ICacheManager cacheManager) {
        log.debug("自动缓存注解已开启支持");
        return new CacheRemoveAdvisor(cacheManager);
    }
}
