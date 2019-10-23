package com.github.gxhunter.threadpool;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

/**
 * @author 树荫下的天空
 * @date 2018/12/25 14:18
 * 线程池
 */
@Configuration
@AutoConfigureAfter(ThreadPoolProperties.class)
@ConditionalOnBean(ThreadPoolProperties.class)
public class ThreadPoolAutoConfig{
    /**
     * 线程池
     * @return
     */
    @Bean
    public ExecutorService threadPool(ThreadPoolProperties threadPoolProperties){
        return new ThreadPoolExecutor(
                threadPoolProperties.getCorePoolSize(),threadPoolProperties.getMaximumPoolSize(),threadPoolProperties.getKeepAliveTime(),TimeUnit.SECONDS
                ,threadPoolProperties.getWorkQueue(),threadPoolProperties.getThreadFactory(),threadPoolProperties.getHandler());
    }
}
