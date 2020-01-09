package com.github.gxhunter.cache;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author wanggx
 * @date 2020-01-03 20:24
 **/
@Component
@AllArgsConstructor
@Slf4j
public class CacheKeyOrTemplate extends AbstractCacheTemplate {
    @Override
    @SneakyThrows
    public <T> T get(String prefix, List<String> key, Type type) {
        for (String redisKey : key) {
            String json;
            if (StringUtils.isNotBlank(prefix)) {
                redisKey = prefix + SPLIT + redisKey;
            }
            json = mRedisTemplate.opsForValue().get(redisKey);
            log.debug("获取缓存，key:{},value:{}",redisKey,json);
            if (StringUtils.isNotBlank(json)) {
                return jsonUtil.parse(json, type);
            }
        }
        return null;
    }

    @Override
    @SneakyThrows
    public void put(String prefix, List<String> keys, Object value, long timeout) {
        String json = jsonUtil.stringify(value);
        if (StringUtils.isBlank(json)) {
            return;
        }
        for (String key : keys) {
            if (StringUtils.isBlank(key)) {
                continue;
            }
            if (StringUtils.isNotBlank(prefix)) {
                key = prefix + SPLIT + key;
            }
            log.debug("存入redis: key {},value {}", key, value);
            mRedisTemplate.opsForValue().set(key, json, timeout, TimeUnit.SECONDS);
        }
    }

    @Override
    public void remove(String prifex, List<String> key) {
        if (prifex != null) {
            key = key.stream().map(k->prifex + SPLIT + k).collect(Collectors.toList());
        }
        log.debug("移除缓存:{}", key);
        mRedisTemplate.delete(key);
    }
}
