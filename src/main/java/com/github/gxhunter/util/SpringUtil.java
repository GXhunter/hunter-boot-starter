package com.github.gxhunter.util;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author wanggx
 * @date 2019/9/18 上午9:07
 */
public class SpringUtil {
    private static ConfigurableApplicationContext context;

    /**
     * @param applicationContext
     */
    public static void setApplicationContext(ConfigurableApplicationContext applicationContext) {
        context = applicationContext;
    }

    /**
     * 通过名字获取上下文中的bean
     *
     * @param name
     * @return
     */
    public static <T> T getBean(String name) {
        return (T) context.getBean(name);
    }


    /**
     * 通过名字获取上下文中的bean
     *
     * @param name
     * @return
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return context.getBean(name, clazz);
    }

    /**
     * 通过类型获取上下文中的bean
     *
     * @param requiredType
     * @return
     */
    public static <T> T getBean(Class<T> requiredType) {
        return context.getBean(requiredType);
    }

    /**
     * 注册bean到spring容器
     *
     * @param beanName       bean名称
     * @param beanDefinition bean定义
     * @see org.springframework.beans.factory.support.BeanDefinitionBuilder
     */
    public static void register(String beanName, BeanDefinition beanDefinition) {
        ((DefaultListableBeanFactory) context.getBeanFactory()).registerBeanDefinition(beanName, beanDefinition);
    }


}
