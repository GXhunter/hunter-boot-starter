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
     * 多个key只要能取到一个
     */
    OR(CacheKeyOrTemplate.class);
    private Class<? extends AbstractCacheTemplate> cacheTemplate;
}
