package com.github.gxhunter.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.github.gxhunter.util.BeanMapper;
import com.github.gxhunter.util.SpringUtil;
import lombok.Builder;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * 默认会注入的bean
 * @author 树荫下的天空
 * @date 2019/9/18 21:41
 */
@Configuration
public class BeanMapperAutoConfig implements ApplicationContextAware{
    @Bean("jsonMapper")
    @ConditionalOnClass(ObjectMapper.class)
    @ConditionalOnMissingBean(name = "jsonMapper")
    @Primary
    public BeanMapper jsonMapper(ObjectMapper objectMapper){
        return new BeanMapper(objectMapper);
    }

    @Bean("yamlUtil")
    @ConditionalOnClass(YAMLMapper.class)
    @ConditionalOnMissingBean(name = "yamlMapper")
    public BeanMapper yamlMapper(Jackson2ObjectMapperBuilder objectMapperBuilder){
        YAMLMapper objectMapper = new YAMLMapper();
        objectMapperBuilder.configure(objectMapper);
        return new BeanMapper(objectMapper);
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException{
        SpringUtil.setApplicationContext((ConfigurableApplicationContext) applicationContext);
    }
}
