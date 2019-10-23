package com.github.gxhunter.webmvc;

import com.github.gxhunter.webmvc.DefaultWebConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author 树荫下的天空
 * @date 2018/12/25 17:37
 * 开启web默认配置
 * bean返回给前端long转string
 * 日期使用yyyy-MM-dd HH:mm:ss
 * 启用默认异常处理器
 * 注：使用此注解后，在yml配置Jackson信息无效
 *
 */
@Import({
        DefaultWebConfig.class
})
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface DefaultWebMVC{

}
