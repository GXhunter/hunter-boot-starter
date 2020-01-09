package com.github.gxhunter.cache;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 添加此注解支持缓存
 * @author wanggx
 * @date 2020-01-09 18:51
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import(CacheAutoConfig.class)
public @interface EnableAutoCache {
}
