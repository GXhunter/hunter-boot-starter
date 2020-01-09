package com.github.gxhunter.cache;

import com.github.gxhunter.util.BeanMapperUtil;
import com.github.gxhunter.util.ConstantValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;

import java.lang.reflect.Type;
import java.util.List;

/**
 * @author wanggx
 * @date 2020-01-03 20:23
 **/
public abstract class AbstractCacheTemplate implements ConstantValue.Cache {
    @Autowired
    protected ApplicationContext mContext;
    @Autowired
    protected RedisTemplate<String, String> mRedisTemplate;
    @Autowired
    protected BeanMapperUtil jsonUtil;

    /**
     * 通过多个key获取缓存目标
     *
     * @param prefix 方法名，拼接到key前面
     * @param key  key
     * @param type 返回类型，支持泛型
     * @return
     * @param <T>
     */
    public abstract <T> T get(String prefix,List<String> key, Type type);

    /**
     * @param prefix 每个key都会有这个前缀
     * @param key
     * @param value
     * @param timeout
     */
    public abstract void put(String prefix,List<String> key, Object value,long timeout);

    /**
     * 移除方法的缓存
     * @param prifex
     * @param key
     */
    public abstract void remove(String prifex, List<String> key);
}
