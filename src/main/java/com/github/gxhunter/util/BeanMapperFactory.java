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
    private static Jackson2ObjectMapperBuilder mMapperBuilder;


    public static BeanMapperUtil getJsonMapper() {
        Assert.notNull(mMapperBuilder);
        if (jsonMapper == null) {
            synchronized (BeanMapperFactory.class) {
                if (jsonMapper == null) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    mMapperBuilder.configure(objectMapper);
                    jsonMapper = new BeanMapperUtil(objectMapper);
                }
            }
        }
        return jsonMapper;
    }

    /**
     * 单例的
     * @return
     */
    public static BeanMapperUtil getYamlMapper() {
        Assert.notNull(mMapperBuilder);
        if (yamlMapper == null) {
            synchronized (BeanMapperFactory.class) {
                if (yamlMapper == null) {
                    YAMLMapper yamlMapper = new YAMLMapper();
                    mMapperBuilder.configure(yamlMapper);
                    BeanMapperFactory.yamlMapper = new BeanMapperUtil(yamlMapper);
                }
            }
        }
        return yamlMapper;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        mMapperBuilder = applicationContext.getBean(Jackson2ObjectMapperBuilder.class);
        if (mMapperBuilder == null) {
            mMapperBuilder = new Jackson2ObjectMapperBuilder();
        }
    }
}
