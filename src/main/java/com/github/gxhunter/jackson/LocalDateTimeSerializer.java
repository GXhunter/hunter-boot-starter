package com.github.gxhunter.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jackson.JacksonProperties;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.TimeZone;

/**
 * @author 树荫下的天空
 * @date 2019/7/31 23:05
 */
public class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime>{
    @Autowired
    private JacksonProperties mJacksonProperties;
    @Override
    public void serialize(LocalDateTime value,JsonGenerator generator,SerializerProvider serializers) throws IOException{
        ZoneId zoneId = Optional.ofNullable(mJacksonProperties)
                .map(JacksonProperties::getTimeZone)
                .map(TimeZone::toZoneId)
                .orElseGet(TimeZone.getDefault()::toZoneId);
        String dateFormat = mJacksonProperties.getDateFormat();
        if(StringUtils.isNotBlank(dateFormat)){
            generator.writeString(DateTimeFormatter.ofPattern(dateFormat).withZone(zoneId).format(value));
        }else {
            generator.writeNumber(value.atZone(zoneId).toInstant().toEpochMilli());
        }
    }
}
