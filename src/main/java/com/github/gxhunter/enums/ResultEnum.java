package com.github.gxhunter.enums;

import com.github.gxhunter.util.ResourceUtil;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ResourceBundle;

/**
 * @author 树荫下的天空
 * @date 2018/9/22 9:43
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ResultEnum implements IResponseCode{
    /**
     * 成功
     */
    public static ResultEnum SUCCESS;
    /**
     * 查询失败
     */
    public static ResultEnum QUERY_FAILURE;
    /**
     * 创建失败
     */
    public static ResultEnum CREATE_FAILURE;
    /**
     * 修改失败
     */
    public static ResultEnum UPDATE_FAILURE;
    /**
     * 删除失败
     */
    public static ResultEnum DELETE_FAILURE;
    /**
     * 默认错误
     */
    public static ResultEnum DEFAULT_ERROR;
    /**
     * 校验失败
     */
    public static ResultEnum METHOD_ARGUMENT_VALID_FAIL;
    /**
     * 其他错误
     */
    public static ResultEnum UNKNOW_ERROR;

    static{
        ResourceUtil ru = new ResourceUtil("response-code");
        SUCCESS = new ResultEnum(ru.getString("SUCCESS"),"成功");
        QUERY_FAILURE = new ResultEnum(ru.getString("QUERY_FAILURE"),"查询不到任何内容");
        CREATE_FAILURE = new ResultEnum(ru.getString("CREATE_FAILURE"),"新建失败");
        UPDATE_FAILURE = new ResultEnum(ru.getString("UPDATE_FAILURE"),"修改失败");
        DELETE_FAILURE = new ResultEnum(ru.getString("DELETE_FAILURE"),"删除失败");
        DEFAULT_ERROR = new ResultEnum(ru.getString("DEFAULT_ERROR"),"操作失败");
        METHOD_ARGUMENT_VALID_FAIL = new ResultEnum(ru.getString("METHOD_ARGUMENT_VALID_FAIL"),"参数校验失败");
        UNKNOW_ERROR = new ResultEnum(ru.getString("UNKNOW_ERROR"),"网络超时");
    }

    private String code;
    private String msg;

    @Override
    public long getCode(){
        return Long.valueOf(code);
    }
}
