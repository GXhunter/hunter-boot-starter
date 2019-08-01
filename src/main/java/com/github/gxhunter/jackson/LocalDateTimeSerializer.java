package com.github.gxhunter.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jackson.JacksonProperties;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * @author 树荫下的天空
 * @date 2019/7/31 23:05
 */
public class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime>{
    @Autowired
    private JacksonProperties mJacksonProperties;
    @Override
    public void serialize(LocalDateTime value,JsonGenerator gen,SerializerProvider serializers) throws IOException{
        gen.writeNumber(value.atZone(mJacksonProperties.getTimeZone().toZoneId()).toInstant().toEpochMilli());
    }
}
