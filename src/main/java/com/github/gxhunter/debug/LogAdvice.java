package com.github.gxhunter.debug;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.Pointcut;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;

/**
 * @author hunter
 */
public class LogAdvice extends AbstractPointcutAdvisor implements MethodInterceptor{

    @Override
    public Pointcut getPointcut(){
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();

        return pointcut;
    }

    @Override
    public Advice getAdvice(){
        return this;
    }

    /**
     * 方法增强
     */
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable{
        return null;
    }
}
