package com.github.gxhunter.config;

import com.github.gxhunter.controller.ExceptionResolveAdvice;
import com.github.gxhunter.controller.ExceptionResolveAdvice2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 树荫下的天空
 * @date 2018/12/25 14:18
 * 线程池
 */
@Configuration
public class ExceptionResolveAutoConfig{
    /**
     * 支持{{@link com.github.gxhunter.controller.BaseController.IfException}}注解
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public ExceptionResolveAdvice exceptionResolveAdvice(){
        return new ExceptionResolveAdvice();
    }

    @Bean
    @ConditionalOnMissingBean
    public ExceptionResolveAdvice2 exceptionResolveAdvice2(){
        return new ExceptionResolveAdvice2();
    }
}
