package com.github.gxhunter.aop;

import com.github.gxhunter.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * @author 树荫下的天空
 * @date 2018/11/27 18:11
 * 打印请求参数
 */
@Slf4j
public class PrintParamAop{
    public Object printRequestParam(ProceedingJoinPoint pjd) throws Throwable{
        // 获取请求的Controller类名
        String className = pjd.getTarget().getClass().getName();
        // 获取执行的方法名称
        String methodName = pjd.getSignature().getName();
        log.debug("---------------打印请求-------------------------/n");
        log.debug(String.format("请求控制器：%s，请求方法：%s",className,methodName));
        for(Object arg : pjd.getArgs()){
            log.debug(JsonUtil.toJSON(arg));
        }
        log.debug("-------------------------------------");
        Object response = pjd.proceed();
        log.debug("---------------打印返回数据------------");
        log.debug(JsonUtil.toJSON(response));
        log.debug("-------------------------------------------");
        return response;
    }
}
