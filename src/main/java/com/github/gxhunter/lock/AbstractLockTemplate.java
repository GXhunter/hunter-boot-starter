package com.github.gxhunter.lock;

import com.github.gxhunter.anno.Lock;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.EvaluationException;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParseException;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 分布式锁
 */
@Slf4j
public abstract class AbstractLockTemplate{
    /**
     * 分布式锁key前缀，避免覆盖已有数据
     */
    private static final String keyPrex = "ec4992a2-2308-460d-ae9e-17982de9e7c1-DistributionLock";

    private static final String SPLIT = ">";

    /**
     * spel表达式解析器
     */
    private static final ExpressionParser PARSER = new SpelExpressionParser();
    private static final ParameterNameDiscoverer NAME_DISCOVERER = new DefaultParameterNameDiscoverer();

    /**
     * 加锁,同时设置锁超时时间
     *
     * @param key        分布式锁的key
     * @param expireTime 单位是ms
     * @return 生成的锁名称，null表示上锁失败
     */
    public abstract String lock(String key,long expireTime);


    /**
     * 解锁
     *
     * @param key 锁
     * @return 是否释放成功
     */
    public abstract boolean unlock(String key,String value);


    /**
     * 生成key,格式为 包名+方法名+key的spel表达式解析结果
     *
     * @param invocation 方法
     * @param lock  注解，用于获取keys信息 通过spel解析
     * @return
     */
    public String generateKeyName(MethodInvocation invocation,Lock lock) {
        Method method = invocation.getMethod();
        if (!ArrayUtils.isEmpty(lock.keys())) {
            return keyPrex + SPLIT + getSpelDefinitionKey(lock.keys(), method, invocation.getArguments());
        }
        return keyPrex + SPLIT + method.getDeclaringClass().getName() + "." + method.getName();
    }

    /**
     * @param expressions     表达式
     * @param method          方法
     * @param parameterValues 参数
     * @return
     */
    private String getSpelDefinitionKey(String[] expressions, Method method, Object[] parameterValues) {
        EvaluationContext context = new MethodBasedEvaluationContext(null, method, parameterValues, NAME_DISCOVERER);
        List<String> definitionKeyList = new ArrayList<>(expressions.length);
        for (String expression : expressions) {
            if (expression != null && !expression.isEmpty()) {
//                表达式解析结果
                String value;
                try {
                    value = Objects.requireNonNull(PARSER.parseExpression(expression).getValue(context)).toString();
                } catch (NullPointerException | EvaluationException | ParseException e) {
                    log.debug("express {} is invalid", expression, e);
                    value = expression;
                }
                definitionKeyList.add(value);
            }
        }

        return definitionKeyList.stream().reduce((a, b) -> a + SPLIT + b).orElse("");
    }
}
