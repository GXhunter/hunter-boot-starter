package com.github.gxhunter.cache;

import com.github.gxhunter.util.ConstantValue;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.util.ByteUtils;

import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * redis缓存管理器
 *
 * @author wanggx
 * @date 2020-01-09 18:39
 **/
@RequiredArgsConstructor
public class RedisCacheManager implements ICacheManager, ConstantValue.Cache{
    private final RedisConnectionFactory mRedisConnectionFactory;
    private final JdkSerializationRedisSerializer mJdkSerializationRedisSerializer;
    private final StringRedisSerializer mKeySerializer;



    @Override
    public void put(Object keyObj,Object object,long timeout){
        byte[] value = mJdkSerializationRedisSerializer.serialize(object);
        byte[] key = Objects.requireNonNull(mKeySerializer.serialize(String.valueOf(keyObj)));
        mRedisConnectionFactory.getConnection().set(key, value, Expiration.from(timeout, TimeUnit.SECONDS), RedisStringCommands.SetOption.upsert());
    }

    @Override
    public void remove(Object keys){
        byte[] keyByte = mKeySerializer.serialize(keys.toString());
        mRedisConnectionFactory.getConnection().del(keyByte);
    }

    @Override
    public  Object get(Object cacheKey){
        byte[] key =  Objects.requireNonNull(mKeySerializer.serialize(String.valueOf(cacheKey)));
        return  mJdkSerializationRedisSerializer.deserialize(mRedisConnectionFactory.getConnection()
                .get(key));
    }

}
