package com.github.gxhunter.enums;

/**
 * @author 树荫下的天空
 * @date 2019/7/27 10:28
 */
public interface IResultCodeAware{
    /**
     * 成功返回
     * @return
     */
    IResponseCode success();

    /**
     * 失败返回
     * @return
     */
    IResponseCode faild();
}
