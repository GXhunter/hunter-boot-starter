package com.github.gxhunter.config;

import com.github.gxhunter.entity.SwaggerInfo;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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


@Configuration
@Import({
        SwaggerInfo.class,
        Swagger2DocumentationConfiguration.class
})
@ConditionalOnProperty(name = "hunter.spring.swagger.enabled")
@ConditionalOnClass(EnableSwagger2.class)
public class SwaggerAutoConfiguration implements BeanFactoryAware{
    private BeanFactory beanFactory;
    @Bean
    public Docket createRestApi(SwaggerInfo swaggerInfo) {
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title(swaggerInfo.getTitle())
                .description(swaggerInfo.getDescription())
                .version(swaggerInfo.getVersion())
                .license(swaggerInfo.getLicense())
                .contact(swaggerInfo.getContact().toContact())
                .licenseUrl(swaggerInfo.getLicenseUrl())
                .build();
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.basePackage(swaggerInfo.getBasePackage()))
                // 扫描该包下的所有需要在Swagger中展示的API，@ApiIgnore注解标注的除外
                .paths(PathSelectors.any())
                .build();
        return docket;
    }


    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException{
        this.beanFactory = beanFactory;
    }
}
