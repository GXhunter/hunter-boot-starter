package com.github.gxhunter.util;

import java.lang.reflect.Field;

/**
 * @author hunter
 * @date 2018.11.27
 */
public class ReflectUtil{

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

}
