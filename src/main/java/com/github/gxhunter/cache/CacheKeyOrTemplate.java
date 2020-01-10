package com.github.gxhunter.cache;

import com.github.gxhunter.util.BeanMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wanggx
 * @date 2020-01-03 20:24
 **/
@Slf4j
public class CacheKeyOrTemplate extends AbstractCacheTemplate {
    public CacheKeyOrTemplate(ApplicationContext mContext, BeanMapper jsonMapper, ICacheManager mCacheManager) {
        super(mContext, jsonMapper, mCacheManager);
    }

    @Override
    @SneakyThrows
    public <T> T get(String prefix, List<String> key, Type type) {
        for (String redisKey : key) {
            if (StringUtils.isNotBlank(prefix)) {
                redisKey = prefix + SPLIT + redisKey;
            }
            T result = mCacheManager.get(redisKey, type);
            log.debug("获取缓存，key:{},value:{}", redisKey, result);
        }
        return null;
    }

    @Override
    @SneakyThrows
    public void put(String prefix, List<String> keys, Object value, long timeout) {
        for (String key : keys) {
            if (StringUtils.isBlank(key)) {
                continue;
            }
            if (StringUtils.isNotBlank(prefix)) {
                key = prefix + SPLIT + key;
            }
            log.debug("存入redis: key {},value {},超时:{}", key, value,timeout);
            mCacheManager.put(key, value, timeout);
        }
    }

    @Override
    public void remove(String prifex, List<String> key) {
        if (prifex != null) {
            key = key.stream().map(k->prifex + SPLIT + k).collect(Collectors.toList());
        }
        log.debug("移除缓存:{}", key);
        mCacheManager.remove(key);
    }
}
