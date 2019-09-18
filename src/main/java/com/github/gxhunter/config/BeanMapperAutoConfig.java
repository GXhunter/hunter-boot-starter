package com.github.gxhunter.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.github.gxhunter.util.BeanMapperUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * 默认会注入的bean
 * @author 树荫下的天空
 * @date 2019/9/18 21:41
 */
@Configuration
public class BeanMapperAutoConfig{
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

}
