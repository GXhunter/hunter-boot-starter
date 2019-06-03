package com.github.gxhunter.util;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import org.springframework.data.redis.core.RedisTemplate;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author hunter
 * @date 2018.11.27
 */
public class ReflectUtil{
    static RedisTemplate redisTemplate;

    /**
     * @param obj  对象
     * @param name 字段
     * @return 该对象属性值
     * @throws IllegalArgumentException
     */
    public static Object getValueByFieldName(Object obj,String name) throws IllegalArgumentException{
        Class<?> clazz = obj.getClass();

        while(clazz != null && !"java.lang.object".equals(clazz.getName().toLowerCase())){
            try{
                Field field = clazz.getDeclaredField(name);
                field.setAccessible(true);
                return field.get(obj);
            }catch(Exception e){
                e.printStackTrace();
            }
            clazz = clazz.getSuperclass();
        }

        return null;
    }


    /**
     * @param interfaceObj 被代理的接口
     * @param handler      回调
     * @param <T>          接口类型
     * @return 接口实现类
     */
    public static <T> T proxy(Class<T> interfaceObj,InvocationHandler handler){
        new InvocationHandler(){

            @Override
            public Object invoke(Object proxy,Method method,Object[] args) throws Throwable{
                return null;
            }
        };

        return interfaceObj.cast(Proxy.newProxyInstance(interfaceObj.getClassLoader(),new Class[]{interfaceObj},handler));
    }

    public static <T> T proxy(Class<T> clazz,MethodInterceptor methodInterceptor){
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(methodInterceptor);
        return (T) enhancer.create();
    }

}
