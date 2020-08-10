package com.github.gxhunter.cache;

import com.github.gxhunter.util.ConstantValue;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

/**
 * 缓存管理器
 *
 * @author wanggx
 * @date 2020-01-09 18:37
 **/
public interface ICacheManager extends ConstantValue.Cache{

    /**
     * 存入缓存
     *
     * @param key     键
     * @param value   值
     * @param timeout 超时 秒
     */
    void put(String key,Object value,long timeout);

    void put(List<String> keyList,Object object,long timeout);

    /**
     * 存入缓存
     *
     * @param key   键
     * @param value 值
     */
    void put(String key,Object value);

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
     * @return 移除
     */
    Long remove(Collection<String> keys);

    /**
     * 获取缓存
     *
     * @param cacheKey 缓存key
     * @param type     返回类型支持泛型
     * @param <T>      泛型
     * @return
     */
    <T> T get(String cacheKey,Type type);

    <T> T get(List<String> cacheKeyList,Type type);

    /**
     * 移除缓存
     *
     * @param prefixList 回调时不会为空
     * @param key        回调时不会为空
     */
    void remove(List<String> prefixList,String key);

}
