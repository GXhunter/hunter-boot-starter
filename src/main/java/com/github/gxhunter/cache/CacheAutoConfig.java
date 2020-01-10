package com.github.gxhunter.cache;

import com.github.gxhunter.util.BeanMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
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
    @ConditionalOnMissingBean
    @ConditionalOnClass(RedisTemplate.class)
    public ICacheManager cacheManager(RedisTemplate<String, String> redisTemplate, BeanMapper jsonMapper) {
        log.debug("当前使用默认RedisTemplate作为缓存工具");
        return new RedisCacheManager(redisTemplate, jsonMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(ICacheManager.class)
    public CacheKeyAndTemplate cacheKeyAndTemplate(ApplicationContext context, BeanMapper jsonMapper, ICacheManager mCacheManager) {
        return new CacheKeyAndTemplate(context,jsonMapper,mCacheManager);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(ICacheManager.class)
    public CacheKeyOrTemplate cacheKeyOrTemplate(ApplicationContext context, BeanMapper jsonMapper, ICacheManager mCacheManager) {
        return new CacheKeyOrTemplate(context,jsonMapper,mCacheManager);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(ICacheManager.class)
    public CacheAdvisor cacheAdvisor(ApplicationContext context) {
        log.debug("自动缓存注解已开启支持");
        return new CacheAdvisor(context);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(ICacheManager.class)
    public CacheRemoveAdvisor cacheRemoveAdvisor(ApplicationContext context) {
        log.debug("自动缓存注解已开启支持");
        return new CacheRemoveAdvisor(context);
    }
}
