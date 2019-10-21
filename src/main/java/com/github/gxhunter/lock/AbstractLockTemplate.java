package com.github.gxhunter.lock;

import com.github.gxhunter.anno.Lock;
import lombok.extern.slf4j.Slf4j;
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
import java.util.UUID;

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
     * 可重入时，实例的唯一标识，与线程id构成redis-value
     */
    private static final String UUID_PREX = UUID.randomUUID().toString();
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
    public abstract String lock(String key,String value,long expireTime);


    /**
     * 解锁
     *
     * @param key 锁
     * @return 是否释放成功
     */
    public abstract boolean unlock(String key,String value);

    /**
     * 分布式锁的value
     * @param reentrant 是否可重入
     * @return 非重入锁时，每次返回的都不一样。可重入锁，相同应用实例的相同线程返回内容一致。
     */
    public String getLockValue(boolean reentrant){
        return reentrant ? UUID_PREX : UUID.randomUUID().toString() + Thread.currentThread().getId();
    }


    /**
     * 生成key,格式为 包名+方法名+key的spel表达式解析结果
     *
     * @param method 方法定义
     * @param args   参数值
     * @param lock   注解，用于获取keys信息 通过spel解析
     * @return redis key名称
     */
    public String getLockKey(Method method,Object[] args,Lock lock){
        String key = keyPrex + SPLIT + method.getDeclaringClass().getName() + "." + method.getName();

        if(!ArrayUtils.isEmpty(lock.keys())){
            EvaluationContext context = new MethodBasedEvaluationContext(null,method,args,NAME_DISCOVERER);
            List<String> definitionKeyList = new ArrayList<>(lock.keys().length);
            for(String expression : lock.keys()){
                if(expression != null && !expression.isEmpty()){
//                表达式解析结果
                    String value;
                    try{
                        value = Objects.requireNonNull(PARSER.parseExpression(expression).getValue(context)).toString();
                    }catch(NullPointerException | EvaluationException | ParseException e){
                        log.debug("express {} is invalid",expression,e);
                        value = expression;
                    }
                    definitionKeyList.add(value);
                }
            }
            key = definitionKeyList.stream().reduce((a,b) -> a + SPLIT + b).orElse("");
        }
        return key;
    }


}
