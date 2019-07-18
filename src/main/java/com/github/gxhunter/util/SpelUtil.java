package com.github.gxhunter.util;

import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.lang.reflect.Method;

/**
 * @author wanggx
 * @date 2019/7/18 14:59
 */
public class SpelUtil{
    /**
     * spel表达式解析器
     */
    private static final ExpressionParser PARSER = new SpelExpressionParser();
    private static final ParameterNameDiscoverer NAME_DISCOVERER = new DefaultParameterNameDiscoverer();

    /**
     * 从方法参数中解析spel表达式，使用参数解析器为：{@link DefaultParameterNameDiscoverer}
     * @param spel       表达式
     * @param method     方法
     * @param returnType 返回类型
     * @return 解析结果
     */
    public static <T> T getValueFromMethod(String spel,Method method,Object[] arguments,Class<T> returnType){
        EvaluationContext context = new MethodBasedEvaluationContext(null,method,arguments,NAME_DISCOVERER);
        return PARSER.parseExpression(spel).getValue(context,returnType);
    }
}
