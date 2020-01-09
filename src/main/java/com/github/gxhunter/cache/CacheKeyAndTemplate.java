package com.github.gxhunter.cache;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author wanggx
 * @date 2020-01-03 20:24
 **/
@Component
@AllArgsConstructor
@Slf4j
public class CacheKeyAndTemplate extends AbstractCacheTemplate {

    @Override
    @SneakyThrows
    public <T> T get(String prefix, List<String> key, Type type) {
        String redisKey;
        if (StringUtils.isNotBlank(prefix)) {
            redisKey = prefix + SPLIT + StringUtils.join(key, SPLIT);
        } else {
            redisKey = StringUtils.join(key, SPLIT);
        }
        String json = mRedisTemplate.opsForValue().get(redisKey);
        log.debug("获取缓存,key:{},value:{}", redisKey, json);
        return jsonUtil.parse(json, type);
    }

    @SneakyThrows
    @Override
    public void put(String prefix, List<String> key, Object value, long timeout) {
        String redisKey;
        if (StringUtils.isNotBlank(prefix)) {
            redisKey = prefix + SPLIT + StringUtils.join(key, SPLIT);
        } else {
            redisKey = StringUtils.join(key, SPLIT);
        }
        String json = jsonUtil.stringify(value);
        if (StringUtils.isNotBlank(json)) {
            log.debug("存入redis：key {}, value {}", redisKey, value);
            mRedisTemplate.opsForValue().set(redisKey, json, timeout, TimeUnit.SECONDS);
        }
    }

    @Override
    public void remove(String prifex, List<String> key) {
        String redisKey;
        if (prifex != null) {
            redisKey = prifex + SPLIT + StringUtils.join(key, SPLIT);
        } else {
            redisKey = StringUtils.join(key, SPLIT);
        }
        log.debug("移除缓存:{}", redisKey);
        mRedisTemplate.delete(redisKey);
    }
}
