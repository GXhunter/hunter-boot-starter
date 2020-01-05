package com.github.gxhunter.jackson;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.LocalDateTime;

/**
 * @author wanggx
 * @date 2019-12-09 15:08
 **/
@Configuration
public class ObjectMapperConfig {
    @Bean
    @Primary
    public ObjectMapper jacksonObjectMapper(Jackson2ObjectMapperBuilder builder, LocalDateTimeSerializer localDateTimeSerializer, LocalDateTimeDeserializer localDateTimeDeserializer) {
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        /*
         * java8 时间日期序列化与反序列化
         */
        javaTimeModule.addDeserializer(LocalDateTime.class,
                localDateTimeDeserializer);
        javaTimeModule.addSerializer(LocalDateTime.class, localDateTimeSerializer);
        //忽略空Bean转json的错误
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS,false);
        //忽略 在json字符串中存在，但是在java对象中不存在对应属性的情况。防止错误
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
//        序列换成json时,将所有的long转成string
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        simpleModule.addSerializer(LocalDateTime.class, localDateTimeSerializer);
        simpleModule.addDeserializer(LocalDateTime.class, localDateTimeDeserializer);
        objectMapper.registerModules( simpleModule);
        return objectMapper;
    }
}
