package com.github.gxhunter.webmvc;

import com.github.gxhunter.entity.ThreadPoolInfo;
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
@AutoConfigureAfter(ThreadPoolInfo.class)
@ConditionalOnBean(ThreadPoolInfo.class)
public class ThreadPoolAutoConfig{
    /**
     * 线程池
     * @return
     */
    @Bean
    public ExecutorService threadPool(ThreadPoolInfo threadPoolInfo){
        return new ThreadPoolExecutor(
                threadPoolInfo.getCorePoolSize(),threadPoolInfo.getMaximumPoolSize(),threadPoolInfo.getKeepAliveTime(),TimeUnit.SECONDS
                ,threadPoolInfo.getWorkQueue(),threadPoolInfo.getThreadFactory(),threadPoolInfo.getHandler());
    }
}
