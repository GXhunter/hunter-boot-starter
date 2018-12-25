package com.github.gxhunter.config;

import com.github.gxhunter.entity.ThreadPoolInfo;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.Data;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

/**
 * @author 树荫下的天空
 * @date 2018/12/25 14:18
 * 线程池
 */
@Configuration
@AutoConfigureAfter(ThreadPoolInfo.class)
@ConditionalOnBean(ThreadPoolInfo.class)
public class ThreadPoolAutoConfig{
    /**
     * 线程池
     * @return
     */
    @Bean
    public ThreadPoolExecutor threadPool(ThreadPoolInfo threadPoolInfo){
        BlockingQueue<Runnable> blockingQueue = new LinkedBlockingQueue<>();
        /*
         * 丢弃并抛异常
         */
        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat("backup-pool-%d")
                .build();

        return new ThreadPoolExecutor(
                threadPoolInfo.getCorePoolSize(),threadPoolInfo.getMaximumPoolSize(),threadPoolInfo.getKeepAliveTime(),TimeUnit.SECONDS
                ,blockingQueue,threadFactory);
    }
}
