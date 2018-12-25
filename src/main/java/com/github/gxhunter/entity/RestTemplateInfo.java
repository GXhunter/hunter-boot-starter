package com.github.gxhunter.entity;
import lombok.Data;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author 树荫下的天空
 * @date 2018/11/27 16:52
 */
@ConfigurationProperties(prefix = "rest")
@Data
@AutoConfigureAfter(HttpMessageConvertersAutoConfiguration.class)
public class RestTemplateInfo{
    /**
     * 读取超时，单位毫秒
     */
    private Integer readTimeout = 5000;
    /**
     * 连接超时 单位毫秒
     */
    private Integer connectTimeout = 3000;
    /**
     * 写入超时 单位毫秒
     */
    private Integer writeTimeout = 5000;
}
