package com.github.gxhunter.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jackson.JacksonProperties;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;

/**
 * @author 树荫下的天空
 * @date 2019/7/31 23:19
 */
public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime>{
    @Autowired
    private JacksonProperties mJacksonProperties;
    @Override
    public LocalDateTime deserialize(JsonParser jsonParser,DeserializationContext ctxt) throws IOException, JsonProcessingException{
        if(jsonParser != null && StringUtils.isNotEmpty(jsonParser.getText())){
            return LocalDateTime.ofInstant(Instant.ofEpochMilli(jsonParser.getLongValue()),mJacksonProperties.getTimeZone().toZoneId());
        }else{
            return null;
        }
    }
}
