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
public interface ICacheManager<K> extends ConstantValue.Cache{

    /**
     * 存入缓存
     *
     * @param key key，为list时多个缓存
     * @param object  待缓存对象
     * @param timeout 超时，如果支持的话
     */
    void put(K key,Object object,long timeout);

    /**
     * 批量移除
     *
     * @param keys 键
     * @return 移除
     */
    Long remove(Collection<String> keys);

    /**
     * 从缓存中获取
     *
     * @param type         类型
     * @param <T>          泛型
     * @return 缓存值
     */
    <T> T get(K key,Type type);

}
