package com.github.gxhunter.entity;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.scheduling.quartz.QuartzJobBean;


/**
 * @author 树荫下的天空
 * @date 2019/2/22 16:42
 */
@Data
@ConfigurationProperties(prefix = "hunter.spring.quartz")
@ConditionalOnClass(QuartzJobBean.class)
@ConditionalOnProperty(name = "hunter.spring.quartz")
public class QuartzInfo{
    /**
     * 配置文件路径
     */
    private String quartzPropertiesName = "/quartz.properties";
    /**
     * 数据源
     */
    private DataSourceProperties dataSource;
}
