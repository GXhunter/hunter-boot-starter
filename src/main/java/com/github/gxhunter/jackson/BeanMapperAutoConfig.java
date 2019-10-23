package com.github.gxhunter.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.github.gxhunter.util.BeanMapperUtil;
import com.github.gxhunter.util.SpringUtil;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * 默认会注入的bean
 * @author 树荫下的天空
 * @date 2019/9/18 21:41
 */
@Configuration
public class BeanMapperAutoConfig implements ApplicationContextAware{
    @Bean("jsonUtil")
    public BeanMapperUtil jsonUtil(Jackson2ObjectMapperBuilder objectMapperBuilder){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapperBuilder.configure(objectMapper);
        return new BeanMapperUtil(objectMapper);
    }

    @Bean("yamlUtil")
    public BeanMapperUtil yamlUtil(Jackson2ObjectMapperBuilder objectMapperBuilder){
        YAMLMapper objectMapper = new YAMLMapper();
        objectMapperBuilder.configure(objectMapper);
        return new BeanMapperUtil(objectMapper);
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException{
        SpringUtil.setApplicationContext((ConfigurableApplicationContext) applicationContext);
    }
}
