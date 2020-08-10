package com.github.gxhunter.cache;

import com.github.gxhunter.util.BeanMapper;
import com.github.gxhunter.util.SpelPaser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
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
    public CacheContextHolder cacheContextHolder() {
        return new CacheContextHolder();
    }

    @Bean
    @ConditionalOnMissingBean(ICacheManager.class)
    @ConditionalOnClass(RedisTemplate.class)
    public ICacheManager redisCacheManager(RedisTemplate<String, String> redisTemplate, BeanMapper jsonMapper) {
        log.debug("当前使用默认RedisTemplate作为缓存工具");
        return new RedisCacheManager(redisTemplate, jsonMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public CacheInterceptor cacheAdvisor(CacheContextHolder cacheContextHolder) {
        int order = Ordered.LOWEST_PRECEDENCE;
        log.debug("自动缓存注解已开启支持,order:{}",order);
        return new CacheInterceptor(cacheContextHolder,order);
    }

    @Bean
    public StringKeyGenerator stringKeyGenerator(){
        SpelPaser spelPaser = new SpelPaser();
        return new StringKeyGenerator(spelPaser);
    }
    @Bean
    public MethodGenericKeyGenerator methodGenericKeyGenerator(){
        return new MethodGenericKeyGenerator();
    }


    @Bean
    @ConditionalOnBean(CacheInterceptor.class)
    @ConditionalOnMissingBean
    public CachePostProcessor cachePostProcessor(CacheContextHolder cacheContextHolder){
        return new CachePostProcessor(cacheContextHolder);
    }
}
