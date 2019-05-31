package com.github.gxhunter.anno;

import org.springframework.stereotype.Component;
import java.lang.annotation.*;

/**
 * @author wanggx
 * @date 2019/5/22 14:28
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface QuartzJob{
    /**
     * cron表达式
     * @return
     */
    String cron();
}
