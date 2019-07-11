package com.github.gxhunter.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;


/**
 * @author wanggx
 * @date 2019/5/27 15:31
 */
@Slf4j
public class ExceptionResolveAdvice2 extends ExceptionResolveAdvice{

    @Override
    public Pointcut getPointcut(){
        return AnnotationMatchingPointcut.forMethodAnnotation(BaseController.IfException.class);
    }

}
