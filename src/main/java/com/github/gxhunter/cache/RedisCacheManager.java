package com.github.gxhunter.cache;

import com.github.gxhunter.util.BeanMapper;
import com.github.gxhunter.util.ConstantValue;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
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
public class RedisCacheManager implements ICacheManager<Object>, ConstantValue.Cache{
    private final RedisTemplate<String, String> mRedisTemplate;
    private final BeanMapper jsonMapper;


    @Override
    public void put(Object keyObj,Object object,long timeout){
        String json = jsonMapper.stringify(object);
        if(keyObj instanceof Collection && !CollectionUtils.isEmpty((Collection<?>) keyObj)){
            for(Object key : ((Collection) keyObj)){
                mRedisTemplate.opsForValue().set(key.toString(),json == null ? CACHE_EMPTY_VALUE : json,timeout,TimeUnit.SECONDS);
            }
        }else{
            mRedisTemplate.opsForValue().set(keyObj.toString(),json == null ? CACHE_EMPTY_VALUE : json,timeout,TimeUnit.SECONDS);
        }

    }

    @Override
    public Long remove(Collection<String> keys){
        if(CollectionUtils.isEmpty(keys)){
            return 0L;
        }
        return mRedisTemplate.delete(keys);
    }

    @Override
    public <T> T get(Object cacheKey,Type type){
        if(cacheKey instanceof Collection && !CollectionUtils.isEmpty((Collection<?>) cacheKey)){
            Collection<?> keyList = (Collection<?>) cacheKey;
            for(Object key : keyList){
                String json = mRedisTemplate.opsForValue().get(key);
                if(StringUtils.equals(json,CACHE_EMPTY_VALUE)){
                    return null;
                }
                if(StringUtils.isNotBlank(json)){
                    return jsonMapper.parse(json,type);
                }
            }
        }
        return null;
    }

}
