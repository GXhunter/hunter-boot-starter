package com.github.gxhunter.vo;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * @author wanggx
 * @date 2019/7/3 15:28
 * 支持多返回值
 */
public class MutiResult<K extends Enum<K>,V>{
    private Map<K, V> resultMap;

    private MutiResult(Map<K, V> resultMap){
        this.resultMap = resultMap;
    }

    /**
     * 获取返回值
     * @param key
     * @return 
     */
    public V get(K key){
        return resultMap.get(key);
    }

    /**
     * 遍历所有返回值
     * @param consumer 执行的逻辑回调
     */
    public void foreach(BiConsumer<K,V> consumer){
        resultMap.forEach(consumer);
    }

    public static <K extends Enum<K>,V> Builder<K,V> build(){
        return new Builder<>();
    }

    /**
     * @param keyClass key的类型
     * @param valueClass value类型
     * @return 建造者对象
     */
    public static <K extends Enum<K>,V> Builder<K,V> build(Class<K> keyClass,Class<V> valueClass){
        return new Builder<>();
    }

    /**
     * 建造者
     *
     * @param <K>
     * @param <V>
     */
    public static final class Builder<K extends Enum<K>,V>{
        private Map<K, V> resultMap;

        Builder(Map<K, V> resultMap){
            this.resultMap = resultMap;
        }

        Builder(){
            this(new HashMap<>(4));
        }

        /**
         * 生成目标类
         *
         * @return
         */
        public MutiResult<K, V> build(){
            return new MutiResult<>(resultMap);
        }

        /**
         * @param key
         * @param value
         * @exception IllegalArgumentException 多次添加同一个key抛出此异常
         * @return
         */
        public Builder<K,V> add(K key,V value){
            if(resultMap.get(key)!=null){
                throw new IllegalArgumentException("重复添加key："+key);
            }
            resultMap.put(key,value);
            return this;
        }
    }
}
