package com.github.gxhunter.controller;

import com.github.gxhunter.enums.IResponseCode;
import com.github.gxhunter.enums.ResultEnum;
import com.github.gxhunter.exception.ApiException;
import com.github.gxhunter.exception.ClassifyException;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;

import java.io.Serializable;

/**
 * @author wanggx
 * @date 2019/5/27 15:31
 */
@Slf4j
public class ExceptionResolveAdvice extends AbstractPointcutAdvisor implements MethodInterceptor{

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
            for(BaseController.IfExceptionInfo exceptionInfo : BaseController.getIfExceptionList(invocation.getMethod())){
                for(Class<? extends Exception> exClazz : exceptionInfo.getWhen()){
                    if(exClazz.isInstance(e)){
                        long errorCode = exceptionInfo.getCode() == -1L ? ResultEnum.DEFAULT_ERROR.getCode() : exceptionInfo.getCode();
                        IResponseCode responseCode = new IResponseCode(){
                            @Override
                            public Serializable getCode(){
                                return errorCode;
                            }
                            @Override
                            public String getMsg(){
                                return exceptionInfo.getValue();
                            }
                        };
                        throw new ClassifyException(responseCode,exClazz);
                    }
                }
            }
            throw e;
        }
    }
}
