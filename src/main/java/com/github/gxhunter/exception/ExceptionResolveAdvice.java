package com.github.gxhunter.exception;

import com.github.gxhunter.controller.AbstractController;
import com.github.gxhunter.controller.BaseController;
import com.github.gxhunter.enums.ResultEnum;
import com.github.gxhunter.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;

/**
 * @author wanggx
 * @date 2019/5/27 15:31
 */
@Slf4j
public class ExceptionResolveAdvice extends AbstractPointcutAdvisor implements MethodInterceptor{

    @Override
    public Pointcut getPointcut(){
        return AnnotationMatchingPointcut.forMethodAnnotation(BaseController.IfException.class);
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
            BaseController.IfException ifException = invocation.getMethod().getAnnotation(AbstractController.IfException.class);
            for(Class<? extends Exception> exClazz : ifException.when()){
                if(exClazz.isInstance(e)){
                    long errorCode = ifException.code() == -1L ? ResultEnum.DEFAULT_ERROR.getCode() : ifException.code();
                    return new Result<>(null,ifException.value(),errorCode);
                }
            }
            throw e;
        }
    }
}
