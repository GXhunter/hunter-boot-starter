package com.github.gxhunter.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * @author wanggx
 * @date 2019/9/17 下午5:33
 */
public class BeanMapperFactory implements ApplicationContextAware {
    private static BeanMapperUtil jsonMapper;
    private static BeanMapperUtil yamlMapper;


    /**
     * 全局单例  饿汉式
     *
     * @return
     */
    public static BeanMapperUtil getJsonMapper() {
        return jsonMapper;
    }

    /**
     * 全局单例  饿汉式
     *
     * @return
     */
    public static BeanMapperUtil getYamlMapper() {
        return yamlMapper;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Jackson2ObjectMapperBuilder mapperBuilder = applicationContext.getBean(Jackson2ObjectMapperBuilder.class);
        if (mapperBuilder == null) {
            mapperBuilder = new Jackson2ObjectMapperBuilder();
        }

        ObjectMapper objectMapper = new ObjectMapper();
        mapperBuilder.configure(objectMapper);
        jsonMapper = new BeanMapperUtil(objectMapper);

        YAMLMapper yamlMapper = new YAMLMapper();
        mapperBuilder.configure(yamlMapper);
        BeanMapperFactory.yamlMapper = new BeanMapperUtil(yamlMapper);
    }
}
