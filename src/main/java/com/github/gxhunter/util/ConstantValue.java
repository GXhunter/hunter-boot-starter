package com.github.gxhunter.util;

import org.springframework.core.Ordered;

/**
 * @author wanggx
 * @date 2020-01-09 14:00
 **/
public interface ConstantValue {
    /**
     * 数字
     */
    interface Number {
        int ZERO = 0;
        int ONE = 1;
        int TWO = 2;
        int THREE = 3;
        int FOUR = 4;
        int FIVE = 5;
        int SIX = 6;
        int SEVEN = 7;
        int EIGHT = 8;
        int NIGHT = 9;
    }

    /**
     * spel表达式相关
     */
    interface Spel {

        /**
         * 注意，仅框架开发者使用
         */
        interface VariableKey {
            /**
             * 方法名
             */
            String METHOD_NAME = "method_name";
            /**
             * 方法完整签名（含泛型）
             */
            String METHOD_GENERIC_SIGN = "method_generic";
            /**
             * 方法返回值（class，不含泛型）
             */
            String METHOD_RETURN_TYPE = "method_return_type";
            /**
             * 方法返回值 含泛型
             */
            String METHOD_RETURN_GEN_TYPE = "method_return_gen_type";

            /**
             * 所属类的类名
             */
            String CLASS_SIMPLE_NAME = "class_simple_name";
        }

        /**
         * 调用者使用，获取spel内置的上下文变量
         */
        interface Context{

            String SHARP = "#";
            /**
             * 方法名
             */
            String METHOD_NAME = SHARP + VariableKey.METHOD_NAME;
            /**
             * 方法完整签名（含泛型）
             */
            String METHOD_GENERIC_SIGN = SHARP + VariableKey.METHOD_GENERIC_SIGN;
            /**
             * 方法返回值（class，不含泛型）
             */
            String METHOD_RETURN_TYPE = SHARP + VariableKey.METHOD_RETURN_TYPE;
            /**
             * 方法返回值 含泛型
             */
            String METHOD_RETURN_GEN_TYPE = SHARP + VariableKey.METHOD_RETURN_GEN_TYPE;
        }


    }

    /**
     * 加密算法算法
     */
    interface MessageAlgorithm {
        String MD5 = "MD5";
    }

    /**
     * 分布式锁
     */
    interface DistributeLock {
        /**
         * 前缀
         */
        String PREFIX = "DistributeLock";
        /**
         * 分隔符
         */
        String SPLIT = "::";
        /**
         * 分布式锁aop优先级
         */
        int AOP_ORDER = Ordered.HIGHEST_PRECEDENCE + 1;
    }

    /**
     * 缓存相关
     */
    interface Cache {
        /**
         * 分隔符
         */
        String SPLIT = "::";
        /**
         * 用于解决redis不能存储null的问题
         */
        String CACHE_EMPTY_VALUE = "cache_empty_value";
        int AOP_ORDER = Ordered.HIGHEST_PRECEDENCE + 2;

    }
}
