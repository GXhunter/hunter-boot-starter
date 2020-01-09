package com.github.gxhunter.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SequenceWriter;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.Collection;

/**
 * @author hunter
 * @date 2018.6.21
 */
@Slf4j
public class BeanMapperUtil {
    private ObjectMapper objectMapper;

    public BeanMapperUtil(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    /**
     * 解析并写入到本地，漂亮缩进方式
     *
     * @param object 对象
     * @param file   文件
     */
    public void writePretty(Object object, File file) {
        if (object == null) {
            return;
        }
        try (
                SequenceWriter writer = objectMapper.writerWithDefaultPrettyPrinter().writeValues(file);
        ) {
            writer.write(object);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析文件并写到输出流里
     *
     * @param object       java对象
     * @param outputStream 输出流
     */
    public void write(Object object, OutputStream outputStream) {
        if (object == null) {
            return;
        }
        try (
                SequenceWriter writer = objectMapper.writer().writeValues(outputStream);
        ) {
            writer.write(object);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析文件并生成文件
     *
     * @param object java对象
     * @param file   生成的文件
     */
    public void write(Object object, File file) {
        if (object == null) {
            return;
        }
        try (
                SequenceWriter writer = objectMapper.writer().writeValues(file);
        ) {
            writer.write(object);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 对象转字符串
     */
    public <T> String stringify(T obj) {
        if (obj == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 对象转字符串
     * 漂亮缩进方式
     */
    public <T> String stringifyPretty(T obj) {
        if (obj == null) {
            return null;
        }
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 字符串转为java对象
     *
     * @param str   json字符串
     * @param clazz java对象类型
     * @param <T>   java对象类型
     * @return
     */
    public <T> T parse(String str, Class<T> clazz) {
        if (StringUtils.isEmpty(str) || clazz == null) {
            return null;
        }

        try {
            return objectMapper.readValue(str, clazz);
        } catch (Exception e) {
            log.error("json反序列化失败，json:{},type:{}",clazz,str,e);
            return null;
        }
    }

    public <T> T parse(String str, TypeReference<T> typeReference) {
        if (StringUtils.isEmpty(str) || typeReference == null) {
            return null;
        }
        try {
            return (T) objectMapper.readValue(str, typeReference);
        } catch (Exception e) {
            log.error("json反序列化失败，json:{},type:{}",typeReference,str,e);
            return null;
        }
    }


    /**
     * 转为集合
     *
     * @param str             json字符串
     * @param collectionClass 集合
     * @param elementClasses  元素
     * @return 对象
     */
    @SafeVarargs
    public final <T extends Collection<E>, E> T parse(String str, Class<T> collectionClass, Class<E>... elementClasses) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
        try {
            return objectMapper.readValue(str, javaType);
        } catch (Exception e) {
            log.error("json反序列化失败，json:{},type:{}",javaType,str,e);
            return null;
        }
    }

    /**
     * 支持泛型的反序列化
     *
     * @param str  json字符串
     * @param type 类型
     * @param <T>  返回类型
     * @return java对象啊
     */
    public final <T> T parse(String str, Type type) {
        if (StringUtils.isEmpty(str) || type == null) {
            return null;
        }
        JavaType javaType = getTypeFactory().constructType(type);
        try {
            return objectMapper.readValue(str, javaType);
        } catch (Exception e) {
            log.error("json反序列化失败，json:{},type:{}",type,str,e);
            return null;
        }
    }

    /**
     * 获取类型构造器
     *
     * @return
     */
    public TypeFactory getTypeFactory() {
        return objectMapper.getTypeFactory();
    }

}
