package com.github.gxhunter.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.github.gxhunter.jackson.LocalDateTimeDeserializer;
import com.github.gxhunter.jackson.LocalDateTimeSerializer;
import com.github.gxhunter.jackson.ResultVoSerializer;
import com.github.gxhunter.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author hunter
 * @date 2018/7/24 15:05
 */
@Configuration
@ConditionalOnMissingBean(WebMvcConfigurationSupport.class)
@ConditionalOnWebApplication
@Import({LocalDateTimeSerializer.class,LocalDateTimeDeserializer.class})
public class DefaultWebConfig extends WebMvcConfigurationSupport{
    @Autowired
    private JacksonProperties mJacksonProperties;
    @Autowired
    private LocalDateTimeSerializer mLocalDateTimeSerializer;
    @Autowired
    private LocalDateTimeDeserializer mLocalDateTimeDeserializer;
    @Autowired
    private ResultVoSerializer mResultVoSerializer;
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        registry.addResourceHandler("doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters){
        MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        /*
         * java8 时间日期序列化与反序列化
         */
        javaTimeModule.addDeserializer(LocalDateTime.class,
                mLocalDateTimeDeserializer);
        javaTimeModule.addSerializer(LocalDateTime.class,mLocalDateTimeSerializer);
        javaTimeModule.addDeserializer(LocalDate.class,new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        javaTimeModule.addSerializer(LocalDate.class,new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        javaTimeModule.addDeserializer(LocalTime.class,new LocalTimeDeserializer(DateTimeFormatter.ofPattern("HH:mm:ss")));
        javaTimeModule.addSerializer(LocalTime.class,new LocalTimeSerializer(DateTimeFormatter.ofPattern("HH:mm:ss")));



        ObjectMapper objectMapper = new ObjectMapper();
//        排除null字段
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//        防止空bean序列化错误
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS,false);
//        忽略 在json字符串中存在，但是在java对象中不存在对应属性的情况。防止错误。
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,false);
        objectMapper.setTimeZone(mJacksonProperties.getTimeZone());
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
//        序列换成json时,将所有的long转成string
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class,ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE,ToStringSerializer.instance);
        simpleModule.addSerializer(Result.class,mResultVoSerializer);
        objectMapper.registerModule(simpleModule);
        objectMapper.registerModule(javaTimeModule);
        jackson2HttpMessageConverter.setObjectMapper(objectMapper);
        converters.add(jackson2HttpMessageConverter);
    }

}
