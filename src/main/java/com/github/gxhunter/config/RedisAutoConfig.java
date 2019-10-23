package com.github.gxhunter.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.Map;
import java.util.UUID;

/**
 * @author 树荫下的天空
 * @date 2018/11/26 22:44
 */
@Configuration
@ConditionalOnClass({RedisTemplate.class,RedisOperations.class,RedisAutoConfiguration.class})
@AutoConfigureBefore({DistributionLockAutoConfig.class,RedisAutoConfiguration.class})
@ConfigurationProperties("hunter.redis")
public class RedisAutoConfig implements ApplicationContextAware{
    /**
     * redis多数据源
     */
    @Setter
    @Getter
    private Map<String, RedisProperties> source;
    private ObjectMapper objectMapper;

    /**
     * spring上下文
     */
    private ConfigurableApplicationContext context;

    /**
     * main redis实例初始化
     * @param factory
     * @return
     */
    @Bean(name = "redisTemplate")
    @SuppressWarnings("all")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // key采用String的序列化方式
        template.setKeySerializer(stringRedisSerializer);
        // hash的key也采用String的序列化方式
        template.setHashKeySerializer(stringRedisSerializer);
        // value序列化方式采用jackson
        template.setValueSerializer(jackson2JsonRedisSerializer);
        // hash的value序列化方式采用jackson
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        initMutil();
        return template;
    }


    public void initMutil() {
        if(CollectionUtils.isEmpty(source)){
            return ;
        }
        source.forEach((beanName,properties) -> {
            /* ========= 基本配置 ========= */
            RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
            configuration.setHostName(properties.getHost());
            configuration.setPort(properties.getPort());
            configuration.setDatabase(properties.getDatabase());
            if(!ObjectUtils.isEmpty(properties.getPassword())){
                RedisPassword redisPassword = RedisPassword.of(properties.getPassword());
                configuration.setPassword(redisPassword);
            }

            /* ========= 连接池通用配置 ========= */
            GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
            if(properties.getLettuce().getPool() != null){
                genericObjectPoolConfig.setMinIdle(properties.getLettuce().getPool().getMinIdle());
                genericObjectPoolConfig.setMaxIdle(properties.getLettuce().getPool().getMaxIdle());
                genericObjectPoolConfig.setMaxWaitMillis(properties.getLettuce().getPool().getMaxWait().toMillis());
//                genericObjectPoolConfig.setMaxTotal();
            }


            /* ========= lettuce pool ========= */
            LettucePoolingClientConfiguration.LettucePoolingClientConfigurationBuilder builder = LettucePoolingClientConfiguration.builder();
            builder.poolConfig(genericObjectPoolConfig);
//            builder.commandTimeout(properties.getTimeout());
            LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory(configuration,builder.build());
            connectionFactory.afterPropertiesSet();

            /* ========= 创建 template ========= */
            RedisTemplate redisTemplate = createRedisTemplate(connectionFactory,objectMapper);
            context.getBeanFactory().registerSingleton(beanName,redisTemplate);
        });
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException{
        this.context = (ConfigurableApplicationContext) applicationContext;
        objectMapper = context.getBean(ObjectMapper.class);

    }


    /**
     * json 实现 redisTemplate
     * <p>
     * 该方法不能加 @Bean 否则不管如何调用，connectionFactory都会是默认配置
     *
     * @param redisConnectionFactory
     * @return
     */
    private RedisTemplate createRedisTemplate(RedisConnectionFactory redisConnectionFactory,ObjectMapper objectMapper){
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;

    }
}
