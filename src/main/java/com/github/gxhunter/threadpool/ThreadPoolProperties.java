package com.github.gxhunter.threadpool;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.AbstractQueue;
import java.util.concurrent.*;

/**
 * @author 树荫下的天空
 * @date 2018/12/25 14:24
 */
@Data
@ConfigurationProperties(prefix = "hunter.spring.thread-pool")
public class ThreadPoolProperties{
    /**
     * 核心线程数
     */
    private int corePoolSize = 6;
    /**
     * 最大线程数
     */
    private int maximumPoolSize = 12;
    /**
     * 当线程数大于核心时，此为终止前多余的空闲线程等待新任务的最长时间。
     */
    private long keepAliveTime = 10L;
    /**
     * 时间单位，默认秒
     */
    private TimeUnit timeUnit = TimeUnit.SECONDS;
    /**
     * 等待队列,默认使用{@link LinkedBlockingQueue}
     */
    private BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>();
    /**
     * 线程工厂
     */
    private ThreadFactory threadFactory = Executors.defaultThreadFactory();
    /**
     * 拒绝策略
     */
    private RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();
}
