package com.github.gxhunter.cache;

import com.github.gxhunter.util.SpelPaser;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author 树荫下的天空
 * @date 2020/8/10 19:07
 */
@AllArgsConstructor
public class StringKeyGenerator implements KeyGenerator{
    private final SpelPaser mSpelPaser;

    @Override
    public Object generate(ProxyMethodMetadata methodMetadata,CacheContext cacheContext,Object returnValue){
        List<String> cacheNames = mSpelPaser.parse(cacheContext.getPrefix(),methodMetadata.getMethod(),methodMetadata.getArgs(),List.class);
        String keys = mSpelPaser.parse(cacheContext.getKey(),methodMetadata.getMethod(),methodMetadata.getArgs(),returnValue,String.class);
        return cacheNames.stream().filter(Objects::nonNull).map(p -> p + "::" + keys).collect(Collectors.toList());
    }
}
