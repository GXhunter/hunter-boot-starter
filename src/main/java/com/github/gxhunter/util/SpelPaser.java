package com.github.gxhunter.util;

import lombok.NoArgsConstructor;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.lang.reflect.Method;
import java.util.Optional;

/**
 * spring el表达式解析器
 *
 * @author wanggx
 * @date 2019/7/18 14:59
 */
@NoArgsConstructor
public class SpelPaser implements ConstantValue.Spel{
    /**
     * spel表达式解析器
     */
    private final ExpressionParser mParser = new SpelExpressionParser();
    private ParameterNameDiscoverer mDiscoverer = new DefaultParameterNameDiscoverer();


    public SpelPaser(ParameterNameDiscoverer discoverer) {
        mDiscoverer = discoverer;
    }

    public <T> T parse(String express, Method method, Object[] args, Class<T> returnType) {
        EvaluationContext context = new MethodBasedEvaluationContext(method, method, args, mDiscoverer);
        context.setVariable(METHOD_NAME,method.getName());
        context.setVariable(METHOD_GENERIC_SIGN,method.toGenericString());
        context.setVariable(METHOD_RETURN_TYPE,method.getReturnType());
        context.setVariable(METHOD_RETURN_GEN_TYPE,method.getGenericReturnType());

        return Optional.ofNullable(express)
                .map(mParser::parseExpression)
                .map(expression->expression.getValue(context, returnType))
                .orElse(null);
    }

    public <T> T parse(String express, EvaluationContext context, Class<T> returnType) {
        return Optional.ofNullable(express)
                .map(mParser::parseExpression)
                .map(expression->expression.getValue(context, returnType))
                .orElse(null);
    }


    public String parse(String express, Method method, Object[] args) {
        return parse(express, method, args, String.class);
    }


}
