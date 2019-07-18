package com.github.gxhunter.util;

import net.sf.cglib.proxy.Enhancer;

import java.lang.reflect.InvocationHandler;
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
            return (T) Proxy.newProxyInstance(clazz.getClassLoader(),clazz.getInterfaces(),handler);
    }

    public static <T> T cglibProxy(Class<T> clazz,InvocationHandler handler){
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        enhancer.setCallback((net.sf.cglib.proxy.InvocationHandler) (proxy,method,args) -> handler.invoke(proxy,method,args));
        return (T) enhancer.create();
    }

}
