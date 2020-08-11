package com.github.gxhunter.cache;

import com.github.gxhunter.util.ConstantValue;

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
     * @param key     key，为list时多个缓存
     * @param object  待缓存对象
     * @param timeout 超时，如果支持的话
     */
    void put(Object key,Object object,long timeout);

    /**
     * 批量移除
     *
     * @param keys 键
     */
    void remove(Object keys);

    /**
     * 从缓存中获取
     *
     * @return 缓存值
     */
    Object get(Object key);

}
