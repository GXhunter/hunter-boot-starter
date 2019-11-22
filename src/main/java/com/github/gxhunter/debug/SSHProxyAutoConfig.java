package com.github.gxhunter.debug;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties(SSHProxyProperties.class)
@Slf4j
@Configuration
@ConditionalOnClass(JSch.class)
@ConditionalOnProperty("hunter.debug.ssh.enabled")
public class SSHProxyAutoConfig {
    @Autowired
    private SSHProxyProperties mProperties;
    @Bean
    public JSch jSch() throws Exception{
        log.info("通过ssh代理已发起，跳板机:{}@{}:{}",mProperties.getTransferUser(),mProperties.getTransferHost(),mProperties.getTransferPort());
        JSch jSch = new JSch();
        jSch.addIdentity(mProperties.getTransferPubKey());
        Session session = jSch.getSession(mProperties.getTransferUser(),mProperties.getTransferHost(),mProperties.getTransferPort());
        session.setConfig("StrictHostKeyChecking","no");
        for(SSHProxyProperties.Proxy proxy : mProperties.getProxyList()){
            session.setPortForwardingL(proxy.getLocalPort(),proxy.getTargetHost(),proxy.getTargetPort());
            log.info("代理映射：{}:{} --> {}:{}",
                    proxy.getTargetHost(),proxy.getTargetPort(),
                    proxy.getLocalhost(),proxy.getLocalPort()
            );
        }
        session.connect();
        log.info("代理完成");
        return jSch;
    }

}
