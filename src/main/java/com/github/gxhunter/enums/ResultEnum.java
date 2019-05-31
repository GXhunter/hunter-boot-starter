package com.github.gxhunter.enums;

import com.github.gxhunter.util.ResourceUtil;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 树荫下的天空
 * @date 2018/9/22 9:43
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ResultEnum implements IResponseCode<Integer>{
    /**
     * 成功
     */
    public final static ResultEnum SUCCESS;
    /**
     * 默认错误(提示操作失败)
     */
    public final static ResultEnum DEFAULT_ERROR;
    /**
     * 校验失败(与@valid一起使用)
     */
    public final static ResultEnum METHOD_ARGUMENT_VALID_FAIL;
    /**
     * 其他错误(提示网络超时)
     */
    public final static ResultEnum UNKNOW_ERROR;

    static{
        ResourceUtil ru = new ResourceUtil("response-code");
        SUCCESS = new ResultEnum(ru.getString("SUCCESS"),"成功");
        DEFAULT_ERROR = new ResultEnum(ru.getString("DEFAULT_ERROR"),"操作失败");
        METHOD_ARGUMENT_VALID_FAIL = new ResultEnum(ru.getString("METHOD_ARGUMENT_VALID_FAIL"),"参数校验失败");
        UNKNOW_ERROR = new ResultEnum(ru.getString("UNKNOW_ERROR"),"网络超时");
    }

    private String code;
    private String msg;

    @Override
    public Integer getCode(){
        return Integer.valueOf(code);
    }
}
