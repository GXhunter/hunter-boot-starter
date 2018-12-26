package com.github.gxhunter.entity;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author 树荫下的天空
 * @date 2018/12/25 14:24
 */
@Data
@ConfigurationProperties(prefix = "hunter.spring.thread-pool")
public class ThreadPoolInfo{
    /**
     * 池中所保存的线程数，包括空闲线程。
     */
    private int corePoolSize = 20;
    /**
     * 池中允许的最大线程数。
     */
    private int maximumPoolSize = 100;
    /**
     * 当线程数大于核心时，此为终止前多余的空闲线 程等待新任务的最长时间。
     */
    private long keepAliveTime = 30L;
}
