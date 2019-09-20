package com.github.gxhunter.controller;

import com.github.gxhunter.result.AResult;
import com.github.gxhunter.jackson.ResultSupport;
import com.github.gxhunter.exception.ClassifyException;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author wanggx
 * @date 2019/5/27 15:31
 */
@Slf4j
public class ExceptionResolveAdvice extends AbstractPointcutAdvisor implements MethodInterceptor{
    @Autowired
    private ResultSupport mResultCodeAware;
    @Override
    public Pointcut getPointcut(){
        return AnnotationMatchingPointcut.forMethodAnnotation(BaseController.ExceptionList.class);
    }

    @Override
    public Advice getAdvice(){
        return this;
    }

    /**
     * 方法增强
     *
     * @param invocation
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable{
        try{
            return invocation.proceed();
        }catch(Exception e){
            for(BaseController.IfExceptionInfo exceptionInfo : BaseController.getIfExceptionList(invocation.getMethod(),invocation.getArguments())){
                for(Class<? extends Exception> exClazz : exceptionInfo.getWhen()){
                    if(exClazz.isInstance(e)){
                        Integer errorCode = exceptionInfo.getCode() == -1L ? mResultCodeAware.exception().getCode() : exceptionInfo.getCode();
                        AResult result = mResultCodeAware.exception().clone();
                        result.setCode(errorCode);
                        result.setMessage(exceptionInfo.getValue());
                        throw new ClassifyException(result,exClazz);
                    }
                }
            }
            throw e;
        }
    }
}
