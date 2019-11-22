package com.github.gxhunter.webmvc;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import springfox.documentation.swagger2.configuration.Swagger2DocumentationConfiguration;


/**
 * @author hunter
 */
@Configuration
@Import({
        Swagger2DocumentationConfiguration.class
})
@EnableConfigurationProperties(SwaggerProperties.class)
@ConditionalOnProperty(name = "hunter.spring.swagger.enabled")
@ConditionalOnClass(EnableSwagger2.class)
public class SwaggerAutoConfiguration implements BeanFactoryAware{
    private BeanFactory beanFactory;
    @Bean
    public Docket createRestApi(SwaggerProperties swaggerProperties) {
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title(swaggerProperties.getTitle())
                .description(swaggerProperties.getDescription())
                .version(swaggerProperties.getVersion())
                .license(swaggerProperties.getLicense())
                .contact(swaggerProperties.getContact().toContact())
                .licenseUrl(swaggerProperties.getLicenseUrl())
                .build();
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.basePackage(swaggerProperties.getBasePackage()))
                // 扫描该包下的所有需要在Swagger中展示的API，@ApiIgnore注解标注的除外
                .paths(PathSelectors.any())
                .build()
                .pathMapping(swaggerProperties.getMappingPath())
                ;
    }


    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException{
        this.beanFactory = beanFactory;
    }
}
