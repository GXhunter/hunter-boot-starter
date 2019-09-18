package com.github.gxhunter.jwt;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * jwt载荷
 * @author wanggx
 * @date 2019/9/18 上午11:33
 */
@Data
public class PlayLoad {
    /**
     * 签发者
     */
    private String iss;

    /**
     * 面向用户
     */
    private String sub;

    /**
     * 接收jwt的一方
     */
    private String aud;

    /**
     * 过期时间
     */
    private LocalDateTime exp;

    /**
     * 在什么时间之前，该jwt都是不可用的.
     */
    private LocalDateTime nbf;

    /**
     * 签发时间
     */
    private LocalDateTime iat;

    /**
     * 唯一身份标识，主要用来作为一次性token,从而回避重放攻击。
     */
    private String jti;
}
