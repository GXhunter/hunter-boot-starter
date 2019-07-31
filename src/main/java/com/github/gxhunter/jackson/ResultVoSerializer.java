package com.github.gxhunter.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.github.gxhunter.controller.BaseController;
import com.github.gxhunter.enums.IResponseCode;
import com.github.gxhunter.enums.IResultCodeAware;
import com.github.gxhunter.vo.Result;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.IOException;

/**
 * @author 树荫下的天空
 * @date 2019/7/31 23:03
 */
@Setter
@Getter
@ConfigurationProperties(prefix = "hunter.spring.result")
public class ResultVoSerializer extends JsonSerializer<Result> implements IResultCodeAware{
    /**
     * 序列化{{@link Result#getCode()}}的key
     */
    private String statusKey = "status";
    /**
     * 序列化{{@link Result#getMessage()}}的key
     */
    private String messageKey = "message";
    /**
     * 序列化{{@link Result#getData()}}的key
     */
    private String dataKey = "data";
    /**
     * 提供一个表示“成功”的返回类到spring上下文。
     * 主要在{{@link BaseController#success()}}使用
     */
    private Result successResult = new Result<>(null,"success",null);
    /**
     * 提供一个表示“失败”的返回类到spring上下文。
     * 主要在{{@link BaseController#faild()}}和异常处理相关使用
     *
     * @see com.github.gxhunter.controller.ExceptionResolveAdvice
     * @see com.github.gxhunter.controller.BaseController.ExceptionList
     * @see com.github.gxhunter.controller.BaseController.IfException
     */
    private Result failedResult = new Result<>(null,"faild",0);

    @Override
    public void serialize(Result value,JsonGenerator gen,SerializerProvider serializers) throws IOException{
        gen.writeStartObject();
        gen.writeNumberField(statusKey,value.getCode());
        gen.writeStringField(messageKey,value.getMessage());
        gen.writeObjectField(dataKey,value.getData());
        gen.writeEndObject();
    }

    @Override
    public IResponseCode success(){
        return successResult;
    }

    @Override
    public IResponseCode faild(){
        return failedResult;
    }
}
