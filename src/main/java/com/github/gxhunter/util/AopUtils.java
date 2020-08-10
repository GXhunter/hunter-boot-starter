package com.github.gxhunter.util;

import org.springframework.aop.IntroductionAwareMethodMatcher;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * @author 树荫下的天空
 * @date 2020/8/5 21:41
 * 完全基于spring
 */
public class AopUtils{
    /**
     * 寻找带有特定注解的方法
     *
     * @param annotation       注解
     * @param targetClass      类
     * @param hasIntroductions 是否查找静态
     * @return 带有注解的方法
     */
    public static List<Method> getTargetMethod(Class<? extends Annotation> annotation,Class<?> targetClass,boolean hasIntroductions){
        AnnotationMatchingPointcut pc = AnnotationMatchingPointcut.forMethodAnnotation(annotation);
        Assert.notNull(pc,"Pointcut must not be null");
        if(!pc.getClassFilter().matches(targetClass)){
            return null;
        }
        List<Method> result = new ArrayList<>();

        MethodMatcher methodMatcher = pc.getMethodMatcher();

        IntroductionAwareMethodMatcher introductionAwareMethodMatcher = null;
        if(methodMatcher instanceof IntroductionAwareMethodMatcher){
            introductionAwareMethodMatcher = (IntroductionAwareMethodMatcher) methodMatcher;
        }

        Set<Class<?>> classes = new LinkedHashSet<>();
        if(!Proxy.isProxyClass(targetClass)){
            classes.add(ClassUtils.getUserClass(targetClass));
        }
        classes.addAll(ClassUtils.getAllInterfacesForClassAsSet(targetClass));

        for(Class<?> clazz : classes){
            Method[] methods = ReflectionUtils.getAllDeclaredMethods(clazz);
            for(Method method : methods){
                if(introductionAwareMethodMatcher != null ?
                        introductionAwareMethodMatcher.matches(method,targetClass,hasIntroductions) :
                        methodMatcher.matches(method,targetClass)){
                    result.add(method);
                }
            }
        }

        return result;
    }
}
