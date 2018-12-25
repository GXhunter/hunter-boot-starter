package com.github.gxhunter.anno;

import com.github.gxhunter.config.SwaggerAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author 树荫下的天空
 * @date 2018/12/25 15:12
 * 开启swagger2
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({SwaggerAutoConfiguration.class})
public @interface EnableApiDoc{

}
