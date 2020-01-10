package com.github.gxhunter.cache;

import com.github.gxhunter.util.BeanMapper;
import com.github.gxhunter.util.ConstantValue;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Type;
import java.util.List;

/**
 * @author wanggx
 * @date 2020-01-03 20:23
 **/
@AllArgsConstructor
public abstract class AbstractCacheTemplate implements ConstantValue.Cache {
    protected ApplicationContext mContext;
    protected BeanMapper jsonMapper;
    protected ICacheManager mCacheManager;

    /**
     * 通过多个key获取缓存目标
     *
     * @param prefix 方法名，拼接到key前面
     * @param key    必须是转化后的，不能是spel表达式
     * @param type   返回类型，支持泛型
     * @param <T>    类型
     * @return 反序列化好的对象
     */
    public abstract <T> T get(String prefix, List<String> key, Type type);

    /**
     * @param prefix  每个key都会有这个前缀
     * @param key     必须是转化后的，不能是spel表达式
     * @param value   对象
     * @param timeout 超时时间
     */
    public abstract void put(String prefix, List<String> key, Object value, long timeout);

    /**
     * 移除方法的缓存
     *
     * @param prifex 必须是转化后的，不能是spel表达式
     * @param key    必须是转化后的，不能是spel表达式
     */
    public abstract void remove(String prifex, List<String> key);
}
