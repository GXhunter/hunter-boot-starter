package com.github.gxhunter.redis;

import io.lettuce.core.resource.ClientResources;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.data.redis.LettuceClientConfigurationBuilderCustomizer;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.util.StringUtils;

import java.net.UnknownHostException;
import java.util.Collections;
import java.util.List;

/**
 * @author wanggx
 * @date 2019-12-04 17:06
 **/
public class LettuceConnectFactory extends RedisConnectionConfiguration{


    private final List<LettuceClientConfigurationBuilderCustomizer> builderCustomizers;

    LettuceConnectFactory(
                                   ObjectProvider<RedisSentinelConfiguration> sentinelConfigurationProvider,
                                   ObjectProvider<RedisClusterConfiguration> clusterConfigurationProvider,
                                   ObjectProvider<List<LettuceClientConfigurationBuilderCustomizer>> builderCustomizers) {
        super( sentinelConfigurationProvider, clusterConfigurationProvider);
        this.builderCustomizers = builderCustomizers
                .getIfAvailable(Collections::emptyList);
    }


    public LettuceConnectionFactory redisConnectionFactory(
            RedisProperties properties,
            ClientResources clientResources) throws UnknownHostException {

        LettuceClientConfiguration clientConfig = getLettuceClientConfiguration(
                properties,clientResources, properties.getLettuce().getPool());
        return createLettuceConnectionFactory(clientConfig,properties);
    }


    private LettuceConnectionFactory createLettuceConnectionFactory(
            LettuceClientConfiguration clientConfiguration,RedisProperties properties) {
        if (getSentinelConfig(properties) != null) {
            return new LettuceConnectionFactory(getSentinelConfig(properties), clientConfiguration);
        }
        if (getClusterConfiguration(properties) != null) {
            return new LettuceConnectionFactory(getClusterConfiguration(properties),
                    clientConfiguration);
        }
        return new LettuceConnectionFactory(getStandaloneConfig(properties), clientConfiguration);
    }

    private LettuceClientConfiguration getLettuceClientConfiguration(
            RedisProperties properties,
            ClientResources clientResources, RedisProperties.Pool pool) {
        LettuceClientConfiguration.LettuceClientConfigurationBuilder builder = createBuilder(pool);
        applyProperties(properties,builder);
        if (StringUtils.hasText(properties.getUrl())) {
            customizeConfigurationFromUrl(properties,builder);
        }
        builder.clientResources(clientResources);
        customize(builder);
        return builder.build();
    }

    private LettuceClientConfiguration.LettuceClientConfigurationBuilder createBuilder(RedisProperties.Pool pool) {
        if (pool == null) {
            return LettuceClientConfiguration.builder();
        }
        return new PoolBuilderFactory().createBuilder(pool);
    }

    private LettuceClientConfiguration.LettuceClientConfigurationBuilder applyProperties(
            RedisProperties properties,
            LettuceClientConfiguration.LettuceClientConfigurationBuilder builder) {
        if (properties.isSsl()) {
            builder.useSsl();
        }
        if (properties.getTimeout() != null) {
            builder.commandTimeout(properties.getTimeout());
        }
        if (properties.getLettuce() != null) {
            RedisProperties.Lettuce lettuce = properties.getLettuce();
            if (lettuce.getShutdownTimeout() != null
                    && !lettuce.getShutdownTimeout().isZero()) {
                builder.shutdownTimeout(
                        properties.getLettuce().getShutdownTimeout());
            }
        }
        return builder;
    }

    private void customizeConfigurationFromUrl(
            RedisProperties properties,
            LettuceClientConfiguration.LettuceClientConfigurationBuilder builder) {
        RedisConnectionConfiguration.ConnectionInfo connectionInfo = parseUrl(properties.getUrl());
        if (connectionInfo.isUseSsl()) {
            builder.useSsl();
        }
    }

    private void customize(
            LettuceClientConfiguration.LettuceClientConfigurationBuilder builder) {
        for (LettuceClientConfigurationBuilderCustomizer customizer : this.builderCustomizers) {
            customizer.customize(builder);
        }
    }

    /**
     * Inner class to allow optional commons-pool2 dependency.
     */
    private static class PoolBuilderFactory {

        public LettuceClientConfiguration.LettuceClientConfigurationBuilder createBuilder(RedisProperties.Pool properties) {
            return LettucePoolingClientConfiguration.builder()
                    .poolConfig(getPoolConfig(properties));
        }

        private GenericObjectPoolConfig getPoolConfig(RedisProperties.Pool properties) {
            GenericObjectPoolConfig config = new GenericObjectPoolConfig();
            config.setMaxTotal(properties.getMaxActive());
            config.setMaxIdle(properties.getMaxIdle());
            config.setMinIdle(properties.getMinIdle());
            if (properties.getMaxWait() != null) {
                config.setMaxWaitMillis(properties.getMaxWait().toMillis());
            }
            return config;
        }

    }

}
