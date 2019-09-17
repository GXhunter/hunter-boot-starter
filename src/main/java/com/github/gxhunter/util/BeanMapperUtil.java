package com.github.gxhunter.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SequenceWriter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

/**
 * @author hunter
 * @date 2018.6.21
 */
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
            return obj instanceof String ? (String) obj : objectMapper.writeValueAsString(obj);
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
            return obj instanceof String ? (String) obj : objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
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
            return clazz.equals(String.class) ? clazz.cast(str) : objectMapper.readValue(str, clazz);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public <T> T parse(String str, TypeReference<T> typeReference) {
        if (StringUtils.isEmpty(str) || typeReference == null) {
            return null;
        }
        try {
            return (T) (typeReference.getType().equals(String.class) ? str : objectMapper.readValue(str, typeReference));
        } catch (Exception e) {
            e.printStackTrace();
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
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
        try {
            return objectMapper.readValue(str, javaType);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
