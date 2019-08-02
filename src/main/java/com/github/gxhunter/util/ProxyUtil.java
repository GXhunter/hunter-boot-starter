package com.github.gxhunter.util;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author wanggx
 * @date 2019/7/15 15:23
 */
public class ProxyUtil{
    /**
     * 创建动态代理对象
     *
     * @param clazz   如果是接口使用JDK动态代理，如果是对象使用cglib
     * @param handler 方法拦截
     * @param <T>     对象类型
     * @return 子类、实现类
     */
    public static <T> T proxy(Class<T> clazz,InvocationHandler handler){
        if(clazz.isInterface()){
            return jdkProxy(clazz,handler);
        }else{
            return cglibProxy(clazz,handler);
        }
    }

    public static <T> T jdkProxy(Class<T> clazz,InvocationHandler handler){
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(),new Class[]{clazz},handler);
    }

    public static <T> T cglibProxy(Class<T> clazz,InvocationHandler handler){
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        enhancer.setCallback((MethodInterceptor) (obj,method,args,proxy) -> {
            if(declaringInObject(method)){
                return proxy.invokeSuper(obj,args);
            }
            return handler.invoke(proxy,method,args);
        });
        return (T) enhancer.create();
    }

    /**
     * @param method 方法
     * @return 是否是从{{@link Object}}继承到的方法
     */
    public static boolean declaringInObject(Method method){
        return method.getDeclaringClass() == Object.class;
    }

}
