package com.github.gxhunter.cache;

import com.github.gxhunter.util.BeanMapper;
import com.github.gxhunter.util.ConstantValue;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * redis缓存管理器
 *
 * @author wanggx
 * @date 2020-01-09 18:39
 **/
@AllArgsConstructor
public class RedisCacheManager implements ICacheManager, ConstantValue.Cache {
    private final RedisTemplate<String, String> mRedisTemplate;
    private final BeanMapper jsonMapper;

    @Override
    public void put(String key, Object object, long timeout) {
        if (StringUtils.isBlank(key) || object == null) {
            return;
        }

        String json = jsonMapper.stringify(object);
        mRedisTemplate.opsForValue().set(key, json == null ? CACHE_EMPTY_VALUE : json, timeout, TimeUnit.SECONDS);
        mRedisTemplate.opsForSet().add(CACHE_KEY_LIST, key);
    }

    @Override
    public void put(List<String> prefixList, String key, Object value, long timeout) {
        for (String prefix : prefixList) {
            String cacheValue = jsonMapper.stringify(value);
            mRedisTemplate.opsForValue().set(prefix + SPLIT + key, cacheValue == null ? CACHE_EMPTY_VALUE : cacheValue, timeout, TimeUnit.SECONDS);
            mRedisTemplate.opsForSet().add(CACHE_KEY_LIST, prefix + SPLIT + key);
        }
    }

    @Override
    public void put(String key, Object object) {
        if (StringUtils.isBlank(key) || object == null) {
            return;
        }

        String json = jsonMapper.stringify(object);
        mRedisTemplate.opsForValue().set(key, json == null ? CACHE_EMPTY_VALUE : json);
        mRedisTemplate.opsForSet().add(CACHE_KEY_LIST, key);
    }

    @Override
    public boolean remove(String key) {
        if (StringUtils.isBlank(key)) {
            return false;
        }
        mRedisTemplate.opsForSet().remove(CACHE_KEY_LIST,  key);
        return mRedisTemplate.delete(key);
    }

    @Override
    public Long remove(Collection<String> keys) {
        if (CollectionUtils.isEmpty(keys)) {
            return 0L;
        }
        Long delete = mRedisTemplate.delete(keys);
        mRedisTemplate.opsForSet().remove(CACHE_KEY_LIST, keys.toArray());
        return delete;
    }

    @Override
    public <T> T get(String cacheKey, Type type) {
        if (StringUtils.isBlank(cacheKey) || type == null) {
            return null;
        }

        String json = mRedisTemplate.opsForValue().get(cacheKey);
        if (StringUtils.equals(json, CACHE_EMPTY_VALUE)) {
            return (T) CACHE_EMPTY_VALUE;
        }
        if (StringUtils.isNotBlank(json)) {
            return jsonMapper.parse(json, type);
        }
        return null;
    }

    @Override
    public <T> T get(List<String> prefixList, String cacheKey, Type type) {
        for (String prefix : prefixList) {
            String json = mRedisTemplate.opsForValue().get(prefix + SPLIT + cacheKey);
            if (StringUtils.equals(json, CACHE_EMPTY_VALUE)) {
                return (T) CACHE_EMPTY_VALUE;
            }
            if (StringUtils.isNotBlank(json)) {
                return jsonMapper.parse(json, type);
            }
        }
        return null;
    }

    @Override
    public void remove(List<String> prefixList, String key) {
        List<String> keys = prefixList.stream().map(prefix -> prefix + SPLIT + key).collect(Collectors.toList());
        mRedisTemplate.opsForSet().remove(CACHE_KEY_LIST, keys.toArray());
        mRedisTemplate.delete(keys);
    }

    @Override
    public Collection<String> listCacheNames() {
        return mRedisTemplate.opsForSet().members(CACHE_KEY_LIST);
    }
}
