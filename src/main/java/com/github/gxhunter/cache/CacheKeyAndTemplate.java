package com.github.gxhunter.cache;

import com.github.gxhunter.util.BeanMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Type;
import java.util.List;

/**
 * @author wanggx
 * @date 2020-01-03 20:24
 **/
@Slf4j
public class CacheKeyAndTemplate extends AbstractCacheTemplate {

    public CacheKeyAndTemplate(ApplicationContext mContext, BeanMapper jsonMapper, ICacheManager mCacheManager) {
        super(mContext, jsonMapper, mCacheManager);
    }

    @Override
    @SneakyThrows
    public <T> T get(String prefix, List<String> key, Type type) {
        String redisKey;
        if (StringUtils.isNotBlank(prefix)) {
            redisKey = prefix + SPLIT + StringUtils.join(key, SPLIT);
        } else {
            redisKey = StringUtils.join(key, SPLIT);
        }
        T result = mCacheManager.get(redisKey, type);
        log.debug("获取缓存,key:{},value:{}", redisKey, result);
        return result;
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
        log.debug("存入redis：key {}, value {},timeouot:{}", redisKey, value, timeout);

        mCacheManager.put(redisKey, value, timeout);
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
        mCacheManager.remove(redisKey);
    }
}
