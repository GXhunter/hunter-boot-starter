package com.github.gxhunter.result;

/**
 *
 * 只有get方法，为“只读”的result，主要用作 1 返回值 2 枚举
 * @author wanggx
 * @date 2019/9/20 上午11:10
 * @see AResult 在此基础上提供set方法和clone原型模式
 */
public interface IResult<T> {
    /**
     * 返回码
     *
     * @return 返回码
     */
    Integer getCode();

    /**
     * 错误描述
     * @return 错误信息
     */
    String getMessage();

    /**
     * 枚举类一般不带此方法
     * @return
     */
    T getData();
}
