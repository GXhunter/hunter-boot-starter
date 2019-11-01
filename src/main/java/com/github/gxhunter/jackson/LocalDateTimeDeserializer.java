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
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;
import java.util.TimeZone;

/**
 * @author 树荫下的天空
 * @date 2019/7/31 23:19
 */
public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime>{
    @Autowired
    private JacksonProperties mJacksonProperties;

    @Override
    public LocalDateTime deserialize(JsonParser jsonParser,DeserializationContext context) throws IOException, JsonProcessingException{
        ZoneId zoneId = Optional.ofNullable(mJacksonProperties)
                .map(JacksonProperties::getTimeZone)
                .map(TimeZone::toZoneId)
                .orElseGet(TimeZone.getDefault()::toZoneId);
        //        以application.yaml配置dateformat格式为准，没配置的话默认序列化为时间戳
        if(StringUtils.isEmpty(mJacksonProperties.getDateFormat())){
            return LocalDateTime.ofInstant(Instant.ofEpochMilli(jsonParser.getLongValue()),zoneId);
        }else{
            String text = jsonParser.getText();
            Locale locale = Optional.ofNullable(mJacksonProperties.getLocale())
                    .orElse(Locale.CHINA);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(mJacksonProperties.getDateFormat(),locale);
            return LocalDateTime.parse(text,formatter);
        }
    }
}
