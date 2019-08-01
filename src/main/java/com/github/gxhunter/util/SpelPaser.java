package com.github.gxhunter.util;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author wanggx
 * @date 2019/7/18 14:59
 */
public class SpelPaser{
    private EvaluationContext mContext;
    /**
     * spel表达式解析器
     */
    private ExpressionParser mParser;
    private ParameterNameDiscoverer mDiscoverer;
    private Pattern mPattern;

    public SpelPaser setContext(Method method,Object[] arguments){
        mContext = new MethodBasedEvaluationContext(null,method,arguments,mDiscoverer);
        return this;
    }

    /**
     * 从方法参数中解析spel表达式，使用参数解析器为：{@link DefaultParameterNameDiscoverer}
     *
     * @param spel       表达式
     * @param returnType 返回类型
     * @return 解析结果
     */
    public <T> T getValueFromMethod(String spel,Class<T> returnType){
        return mParser.parseExpression(spel).getValue(mContext,returnType);
    }

    /**
     * 解析字符串中的el表达式
     *
     * @param value
     * @return
     */
    public String parse(String value){
        Assert.notNull(mPattern);
        String el;
        StringBuilder result = new StringBuilder();
        Matcher matcher = mPattern.matcher(value);
        for(int i = 0, len = value.length(); i < len; ){
            if(matcher.find(i)){
                int start = matcher.start();
                int end = matcher.end();
                if(start != i){
                    result.append(value,i,start);
                }
                el = value.substring(start,end).replaceAll("[{}]","");
                result.append(getValueFromMethod(el,String.class));
                i = end;
            }else{
                result.append(value,i,len);
                i += len;
            }
        }
        return result.toString();
    }

    public static Builder builder(){
        return new Builder();
    }

    public static Builder builder(EvaluationContext context){
        return new Builder().context(context);
    }

    public static class Builder{
        private EvaluationContext context;
        private ExpressionParser parse = new SpelExpressionParser();
        private ParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();
        private String regExp;
        public Builder context(EvaluationContext context){
            this.context = context;
            return this;
        }

        public Builder regExp(String regExp){
            this.regExp = regExp;
            return this;
        }

        public Builder parse(ExpressionParser parse){
            this.parse = parse;
            return this;
        }

        public Builder nameDiscoverer(ParameterNameDiscoverer nameDiscoverer){
            this.nameDiscoverer = nameDiscoverer;
            return this;
        }

        public SpelPaser build(){
            SpelPaser spelPaser = new SpelPaser();
            spelPaser.mContext = context;
            spelPaser.mParser = parse;
            spelPaser.mDiscoverer = nameDiscoverer;
            spelPaser.mPattern = Pattern.compile(regExp);
            return spelPaser;
        }
    }
}
