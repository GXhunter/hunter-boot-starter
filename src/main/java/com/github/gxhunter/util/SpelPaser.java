package com.github.gxhunter.util;

import lombok.NoArgsConstructor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

/**
 * spring el表达式解析器
 *
 * @author wanggx
 * @date 2019/7/18 14:59
 */
@NoArgsConstructor
public class SpelPaser implements ConstantValue.Spel.VariableKey {
    /**
     * spel表达式解析器
     */
    private final ExpressionParser mParser = new SpelExpressionParser();
    private ParameterNameDiscoverer mDiscoverer = new DefaultParameterNameDiscoverer();
    private final EvaluationContext standardEvaluationContext = new StandardEvaluationContext();

    public SpelPaser(ParameterNameDiscoverer discoverer) {
        mDiscoverer = discoverer;
    }

    /**
     * 标准解析器解析的表达式
     *
     * @param express    el表达式
     * @param returnType 返回类型
     * @param <T> 泛型
     * @return 解析结果
     */
    public <T> T parse(String express, Class<T> returnType) {
        return mParser.parseExpression(express).getValue(standardEvaluationContext, returnType);
    }

    public <T> T parse(String express, MethodInvocation invocation, Class<T> returnType) {
        return parse(express, invocation.getMethod(), invocation.getArguments(), returnType);
    }

    /**
     * @param express    表达式
     * @param method     方法上下文
     * @param args       方法参数
     * @param returnType 解析的结果类型
     * @param <T>        泛型
     * @return 解析结果
     */
    public <T> T parse(String express, Method method, Object[] args, Class<T> returnType) {
        EvaluationContext context = new MethodBasedEvaluationContext(method, method, args, mDiscoverer);
        context.setVariable(METHOD_NAME, method.getName());
        context.setVariable(METHOD_GENERIC_SIGN, method.toGenericString());
        context.setVariable(METHOD_RETURN_TYPE, method.getReturnType());
        context.setVariable(METHOD_RETURN_GEN_TYPE, method.getGenericReturnType());
        context.setVariable(CLASS_SIMPLE_NAME, method.getDeclaringClass().getSimpleName());
        return Optional.ofNullable(express)
                .map(mParser::parseExpression)
                .map(expression -> expression.getValue(context, returnType))
                .orElse(null);
    }

    public <T> T parse(String express, EvaluationContext context, Class<T> returnType) {
        return Optional.ofNullable(express)
                .map(mParser::parseExpression)
                .map(expression -> expression.getValue(context, returnType))
                .orElse(null);
    }


    public String parse(String express, Method method, Object[] args) {
        return StringUtils.join(parse(express, method, args, List.class),ConstantValue.Spel.SPLIT);
    }


}
