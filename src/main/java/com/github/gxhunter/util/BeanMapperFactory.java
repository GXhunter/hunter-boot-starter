package com.github.gxhunter.util;

import com.github.gxhunter.jackson.BeanMapperAutoConfig;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Import;

/**
 * @author wanggx
 * @date 2019/9/17 下午5:33
 */
@Import(BeanMapperAutoConfig.class)
public class BeanMapperFactory implements ApplicationContextAware{
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
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        jsonMapper = context.getBean("jsonUtil",BeanMapperUtil.class);
        yamlMapper = context.getBean("yamlUtil",BeanMapperUtil.class);
    }
}
