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

}
