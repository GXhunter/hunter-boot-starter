package com.github.gxhunter.debug;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author hunter
 */
@ConfigurationProperties("hunter.debug.ssh")
@Data
public class SSHProxyProperties{
    /**
     * 是否开启代理
     */
    private boolean enabled = false;

    /**
     * 跳板机ip
     */
    private String transferHost;
    /**
     * 跳板机端口
     */
    private int transferPort = 22;
    /**
     * 跳板机用户名
     */
    private String transferUser;

    /**
     * 跳板机公钥地址
     */
    private String transferPubKey;

    /**
     * 代理列表
     */
    private List<Proxy> proxyList;

    @Setter
    @Getter
    static class Proxy{
        /**
         * 本地host
         */
        private String localhost = "127.0.0.1";
        /**
         * 本地端口
         */
        private int localPort = 3306;

        /**
         * 目标ip
         */
        private String targetHost;
        /**
         * 目标端口
         */
        private int targetPort;

    }
}
