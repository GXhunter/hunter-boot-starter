package com.github.gxhunter.cache;

import com.github.gxhunter.util.ConstantValue;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

/**
 * 缓存管理器
 * @author wanggx
 * @date 2020-01-09 18:37
 **/
public interface ICacheManager extends ConstantValue.Cache {

    /**
     * 存入缓存
     *
     * @param key     键
     * @param value   值
     * @param timeout 超时 秒
     */
    void put(String key, Object value, long timeout);

    /**
     * @param prefix  前缀，几个前缀就存几个缓存。回调此方法保证不会{{@link org.springframework.util.CollectionUtils#isEmpty(Collection)}}
     * @param key     key
     * @param value   缓存值，java对象。
     * @param timeout 超时
     */
    void put(List<String> prefix, String key, Object value, long timeout);

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
     * @param type     返回类型支持泛型
     * @param <T>
     * @return
     */
    <T> T get(String cacheKey, Type type);

    /**
     * @param prefix   回调时不会空
     * @param cacheKey 回调时不会是空
     * @param type     返回类型
     * @param <T>      外层类型
     * @return
     */
    <T> T get(List<String> prefix, String cacheKey, Type type);

    /**
     * @param prefixList 回调时不会为空
     * @param key 回调时不会为空
     */
    void remove(List<String> prefixList, String key);
}
