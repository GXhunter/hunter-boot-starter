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

        public Builder<K,V> add(K key,V value){
            resultMap.put(key,value);
            return this;
        }
    }
}
