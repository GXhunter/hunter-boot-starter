package com.github.gxhunter.cache;

import java.lang.reflect.Type;
import java.util.Collection;

/**
 * @author wanggx
 * @date 2020-01-09 18:37
 **/
public interface ICacheManager {

    /**
     * 存入缓存
     *
     * @param key     键
     * @param value   值
     * @param timeout 超时 秒
     */
    void put(String key, Object value, long timeout);

    /**
     * 存入缓存
     *
     * @param key   键
     * @param value 值
     */
    void put(String key, Object value);

    /**
     * 删除缓存
     *
     * @param key 键
     * @return 是否移除成功
     */
    boolean remove(String key);

    /**
     * 批量移除
     *
     * @param keys 键
     * @return
     */
    long remove(Collection<String> keys);

    /**
     * 获取缓存
     *
     * @param cacheKey
     * @param type 返回类型支持泛型
     * @param <T>
     * @return
     */
    <T> T get(String cacheKey, Type type);
}
