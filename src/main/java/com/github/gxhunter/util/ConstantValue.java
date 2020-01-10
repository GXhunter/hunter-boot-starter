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
        int AOP_ORDER = Ordered.HIGHEST_PRECEDENCE + 2;
    }
}
