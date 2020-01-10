package com.github.gxhunter.cache;

import com.github.gxhunter.util.BeanMapper;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * @author wanggx
 * @date 2020-01-09 18:39
 **/
@AllArgsConstructor
public class RedisCacheManager implements ICacheManager {
    private final RedisTemplate<String, String> mRedisTemplate;
    protected final BeanMapper jsonMapper;

    @Override
    public void put(String key, Object object, long timeout) {
        if (StringUtils.isBlank(key) || object == null) {
            return;
        }

        String json = jsonMapper.stringify(object);
        if (StringUtils.isNotBlank(json)) {
            mRedisTemplate.opsForValue().set(key, json, timeout, TimeUnit.SECONDS);
        }
    }

    @Override
    public void put(String key, Object object) {
        if (StringUtils.isBlank(key) || object == null) {
            return;
        }
        String json = jsonMapper.stringify(object);
        if (StringUtils.isNotBlank(json)) {
            mRedisTemplate.opsForValue().set(key, json);
        }
    }

    @Override
    public boolean remove(String key) {
        if (StringUtils.isBlank(key)) {
            return false;
        }
        return mRedisTemplate.delete(key);
    }

    @Override
    public long remove(Collection<String> keys) {
        if ( CollectionUtils.isEmpty(keys)) {
            return 0L;
        }
        return mRedisTemplate.delete(keys);
    }

    @Override
    public <T> T get(String cacheKey, Type type) {
        if (StringUtils.isBlank(cacheKey) || type == null) {
            return null;
        }

        String json = mRedisTemplate.opsForValue().get(cacheKey);
        if (StringUtils.isNotBlank(json)) {
            return jsonMapper.parse(json, type);
        }
        return null;
    }
}
