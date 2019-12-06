package com.github.gxhunter.result;

/**
 * @author 树荫下的天空
 * @date 2019/7/27 10:28
 */
public interface ResultSupport {
    /**
     * 成功返回
     *
     * @return
     */
    Result success();

    /**
     * 失败返回
     *
     * @return
     */
    Result faild();

    /**
     * 异常返回，异常处理器拦截到异常时的返回，默认和{{@link #faild()}}一样
     *
     * @return
     */
    Result exception();
}
