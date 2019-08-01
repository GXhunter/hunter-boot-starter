package com.github.gxhunter.jackson;

import com.github.gxhunter.enums.IResult;

/**
 * @author 树荫下的天空
 * @date 2019/7/27 10:28
 */
public interface IResultCodeAware{
    /**
     * 成功返回
     *
     * @return
     */
    IResult success();

    /**
     * 失败返回
     *
     * @return
     */
    IResult faild();

    /**
     * 异常返回，异常处理器拦截到异常时的返回，默认和{{@link #faild()}}一样
     *
     * @return
     */
    IResult exception();

    Class<? extends IResult> resultClass();
}
