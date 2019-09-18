package com.github.gxhunter.jwt;

import lombok.Data;

/**
 * jwt 头部
 * @author wanggx
 * @date 2019/9/18 上午11:37
 */
@Data
public class JwtHeader {
    /**
     * 加密算法
     */
    private String alg;

    /**
     * 声明类型
     */
    private String typ;
}
