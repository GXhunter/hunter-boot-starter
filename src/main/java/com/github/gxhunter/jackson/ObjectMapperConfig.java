package com.github.gxhunter.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
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
