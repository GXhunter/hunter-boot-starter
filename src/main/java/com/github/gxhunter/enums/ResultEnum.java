package com.github.gxhunter.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 树荫下的天空
 * @date 2018/9/22 9:43
 */
@AllArgsConstructor
@Getter
public enum ResultEnum implements IResponseCode{
    SUCCESS(0,"成功"),

    QUERY_FAILURE(1,"查询不到任何内容"),
    CREATE_FAILURE(2,"新建失败"),
    UPDATE_FAILURE(3,"修改失败"),
    DELETE_FAILURE(4,"删除失败"),


    DEFAULT_ERROR(1000,"操作失败"),
    METHOD_ARGUMENT_VALID_FAIL(1001,"参数校验失败"),
    UNKNOW_ERROR(1999,"网络超时"),
    ;
    private long code;
    private String msg;

}
