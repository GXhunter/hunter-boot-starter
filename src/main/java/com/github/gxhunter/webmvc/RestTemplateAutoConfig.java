package com.github.gxhunter.webmvc;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @author 树荫下的天空
 * @date 2018/11/27 16:12
 */
@Configuration
@AutoConfigureAfter(HttpMessageConvertersAutoConfiguration.class)
@ConditionalOnClass(RestTemplate.class)
@ConditionalOnMissingBean(RestTemplate.class)
public class RestTemplateAutoConfig{

    @Bean
    RestTemplate restTemplate(ClientHttpRequestFactory simpleClientHttpRequestFactory){
        return new RestTemplate(simpleClientHttpRequestFactory);
    }

    @Bean
    @SuppressWarnings("all")
    public ClientHttpRequestFactory simpleClientHttpRequestFactory(RestTemplateProperties info){
        OkHttp3ClientHttpRequestFactory factory = new OkHttp3ClientHttpRequestFactory();
        factory.setReadTimeout(info.getReadTimeout());
        factory.setConnectTimeout(info.getConnectTimeout());
        factory.setWriteTimeout(info.getWriteTimeout());
        return factory;
    }
}
