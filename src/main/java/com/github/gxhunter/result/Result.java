package com.github.gxhunter.result;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.SneakyThrows;

import java.io.Serializable;

/**
 * 微服务之间、前后端交互的基本对象
 * @author 树荫下的天空
 * @date 2018/5/31 14:49
 * @see Cloneable 支持原型模式创建对象
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result<T>  implements Cloneable, Serializable {
    private Integer code;
    private String message;
    private T data;

    public Result(){}

    public Result(T data,String message,Integer code){
        this.data = data;
        this.message = message;
        this.code = code;
    }
    @SneakyThrows
    @Override
    public Result<T> clone() {
        return (Result<T>) super.clone();
    }
}
