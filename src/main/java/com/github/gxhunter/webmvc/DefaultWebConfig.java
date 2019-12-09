package com.github.gxhunter.webmvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.gxhunter.jackson.LocalDateTimeDeserializer;
import com.github.gxhunter.jackson.LocalDateTimeSerializer;
import com.github.gxhunter.jackson.ObjectMapperConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

/**
 * @author hunter
 * @date 2018/7/24 15:05
 */
@Configuration
@ConditionalOnMissingBean(WebMvcConfigurationSupport.class)
@ConditionalOnWebApplication
@Import({LocalDateTimeSerializer.class,LocalDateTimeDeserializer.class, ObjectMapperConfig.class})
public class DefaultWebConfig extends WebMvcConfigurationSupport{
    @Autowired
    private ObjectMapper objectMapper;
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
        jackson2HttpMessageConverter.setObjectMapper(objectMapper);
        converters.add(jackson2HttpMessageConverter);
    }

}
