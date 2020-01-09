package com.github.gxhunter.cache;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author wanggx
 * @date 2020-01-03 20:20
 **/
@AllArgsConstructor
@Getter
public enum CacheStrategy {
    /**
     *所有key以特定规则组合而成
     */
    AND(CacheKeyAndTemplate.class),
    /**
     * 读取时多个key按序读取，只要读取到一个非null就返回
     * 写入时，有几个key就存几条
     */
    OR(CacheKeyOrTemplate.class);

    private Class<? extends AbstractCacheTemplate> cacheTemplate;
}
