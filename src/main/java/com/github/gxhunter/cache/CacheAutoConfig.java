package com.github.gxhunter.cache;

import com.github.gxhunter.util.BeanMapperUtil;
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
public class CacheAutoConfig {

    @Bean
    @ConditionalOnMissingBean
    public ICacheManager cacheManager(RedisTemplate<String, String> redisTemplate,BeanMapperUtil jsonUtil) {
        return new RedisCacheManager(redisTemplate, jsonUtil);
    }

    @Bean
    @ConditionalOnMissingBean
    public CacheKeyAndTemplate cacheKeyAndTemplate(ApplicationContext context, BeanMapperUtil jsonUtil, ICacheManager mCacheManager) {
        return new CacheKeyAndTemplate(context,jsonUtil,mCacheManager);
    }

    @Bean
    @ConditionalOnMissingBean
    public CacheKeyOrTemplate cacheKeyOrTemplate(ApplicationContext context, BeanMapperUtil jsonUtil, ICacheManager mCacheManager) {
        return new CacheKeyOrTemplate(context,jsonUtil,mCacheManager);
    }

    @Bean
    @ConditionalOnMissingBean
    public CacheAdvisor cacheAdvisor(ApplicationContext context) {
        return new CacheAdvisor(context);
    }

    @Bean
    @ConditionalOnMissingBean
    public CacheRemoveAdvisor cacheRemoveAdvisor(ApplicationContext context) {
        return new CacheRemoveAdvisor(context);
    }
}
