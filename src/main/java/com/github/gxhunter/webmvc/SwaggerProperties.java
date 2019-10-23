package com.github.gxhunter.webmvc;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hunter
 */
@Data
@ConfigurationProperties("hunter.spring.swagger")
public class SwaggerProperties{
    /**
     * 是否开启swagger
     **/
    private Boolean enabled;

    /**
     * 标题
     **/
    private String title = "";
    /**
     * 描述
     **/
    private String description = "";
    /**
     * 版本
     **/
    private String version = "0.1";
    /**
     * 许可证
     **/
    private String license = "";
    /**
     * 许可证URL
     **/
    private String licenseUrl = "";

    /**
     * swagger会解析的包路径
     **/
    private String basePackage = "";

    /**
     * 在basePath基础上需要排除的url规则
     **/
    private List<String> excludePath = new ArrayList<>();

    /**
     * host信息
     **/
    private String host = "";

    /**
     * 作者信息
     */
    private Contact contact = new Contact();

    @Data
    public static class Contact{
        /**
         * 联系人
         **/
        private String name = "";
        /**
         * 联系人url
         **/
        private String url = "";
        /**
         * 联系人email
         **/
        private String email = "";
        public springfox.documentation.service.Contact toContact(){
            return new springfox.documentation.service.Contact(name,url,email);
        }
    }
}


