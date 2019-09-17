package com.github.gxhunter.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SequenceWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.Collection;

/**
 * @author 树荫下的天空
 * @date 2019/9/16 21:35
 */
public class YamlUtil{
    private static YAMLMapper objectMapper = new YAMLMapper();

    static{
        //只序列化不为null的字段
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //取消默认转换timestamps形式
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,false);
        //忽略空Bean转yaml的错误
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS,false);
        //忽略 在yaml字符串中存在，但是在java对象中不存在对应属性的情况。防止错误
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
    }

    /**
     * 对象转yaml
     */
    public static <T> String toYml(T obj){
        if(obj == null){
            return null;
        }
        try{
            return obj instanceof String ? (String) obj : objectMapper.writeValueAsString(obj);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 对象转yaml数组
     */
    public static <T> String toYmlPretty(T obj){
        if(obj == null){
            return null;
        }
        try{
            return obj instanceof String ? (String) obj : objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 对象转yml并存储到本地
     *
     * @param obj  java对象
     * @param file 文件
     * @return
     */
    public static void write(Object obj,File file){
        if(obj == null){
            return ;
        }
        try(
                SequenceWriter sequenceWriter = objectMapper.writerWithDefaultPrettyPrinter()
                        .writeValues(file);
        ){
            sequenceWriter.write(obj);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * JSON转为java对象
     *
     * @param str   yaml字符串
     * @param clazz java对象类型
     * @param <T>   java对象类型
     * @return
     */
    public static <T> T parse(String str,Class<T> clazz){
        if(StringUtils.isEmpty(str) || clazz == null){
            return null;
        }

        try{
            return clazz.equals(String.class) ? clazz.cast(str) : objectMapper.readValue(str,clazz);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T parse(String str,TypeReference<T> typeReference){
        if(StringUtils.isEmpty(str) || typeReference == null){
            return null;
        }
        try{
            return (T) (typeReference.getType().equals(String.class) ? str : objectMapper.readValue(str,typeReference));
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 转为集合
     *
     * @param str             yaml字符串
     * @param collectionClass 集合
     * @param elementClasses  元素
     * @return 对象
     */
    @SafeVarargs
    public static <T extends Collection<E>,E> T parse(String str,Class<T> collectionClass,Class<E>... elementClasses){
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(collectionClass,elementClasses);
        try{
            return objectMapper.readValue(str,javaType);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

}
