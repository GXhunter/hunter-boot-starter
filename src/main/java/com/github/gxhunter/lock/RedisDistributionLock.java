package com.github.gxhunter.lock;

import com.github.gxhunter.anno.RedisLock;
import com.github.gxhunter.util.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.EvaluationException;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParseException;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * redis分布式锁
 *
 * @author wanggx
 * @date 2019/5/24 17:17
 */
@Slf4j
public class RedisDistributionLock{

    /**
     * redis操作客户端
     */
    private RedisTemplate<String, Object> mRedisTemplate;

    /**
     * spel表达式解析器
     */
    private static final ExpressionParser PARSER = new SpelExpressionParser();
    private static final ParameterNameDiscoverer NAME_DISCOVERER = new DefaultParameterNameDiscoverer();
    /**
     * 加锁LUA脚本
     */
    private static final RedisScript<String> SCRIPT_LOCK = new DefaultRedisScript<>("return redis.call('set',KEYS[1],ARGV[1],'NX','PX',ARGV[2])",String.class);
    /**
     * 释放锁LUA脚本
     */
    private static final RedisScript<String> SCRIPT_UNLOCK = new DefaultRedisScript<>("if redis.call('get',KEYS[1]) == ARGV[1] then return tostring(redis.call('del', KEYS[1])==1) else return 'false' end",String.class);
    private static final String LOCK_SUCCESS = "OK";


    public RedisDistributionLock(RedisTemplate<String, Object> redisTemplate){
        mRedisTemplate = redisTemplate;
    }

    /**
     * 生成key,格式为 包名+方法名+key的spel表达式解析结果
     *
     * @param invocation 方法
     * @param redisLock  注解，用于获取keys信息 通过spel解析
     * @return
     */
    public String generateKeyName(MethodInvocation invocation,RedisLock redisLock){
        StringBuilder sb = new StringBuilder();
        Method method = invocation.getMethod();
        sb.append(method.getDeclaringClass().getName()).append(".").append(method.getName());
        if(redisLock.keys().length > 1 || !"".equals(redisLock.keys()[0])){
            sb.append(getSpelDefinitionKey(redisLock.keys(),method,invocation.getArguments()));
        }
        return sb.toString();
    }

    /**
     * @param expressions     表达式
     * @param method          方法
     * @param parameterValues 参数
     * @return
     */
    private String getSpelDefinitionKey(String[] expressions,Method method,Object[] parameterValues){
        EvaluationContext context = new MethodBasedEvaluationContext(null,method,parameterValues,NAME_DISCOVERER);
        List<String> definitionKeyList = new ArrayList<>(expressions.length);
        for(String expression : expressions){
            if(expression != null && !expression.isEmpty()){
//                表达式解析结果
                String value;
                try{
                    value = PARSER.parseExpression(expression).getValue(context).toString();
                }catch(EvaluationException | ParseException e){
                    log.debug("express {} is invalid",expression);
                    value = expression;
                }
                definitionKeyList.add(value);
            }
        }

        return definitionKeyList.stream().reduce((a,b) -> a + "." + b).orElse("");
//        return StringUtils.collectionToDelimitedString(definitionKeyList,".","","");
    }

    private String generateLockValue(){
        return UUID.randomUUID().toString() + "-" + Thread.currentThread().getId();
    }


    /**
     * 加锁,同时设置锁超时时间
     *
     * @param key        分布式锁的key
     * @param expireTime 单位是ms
     * @return 生成的锁名称
     */
    public String lock(String key,long expireTime){
        String lockValue = generateLockValue();
        Object lockResult = mRedisTemplate.execute(SCRIPT_LOCK,
                mRedisTemplate.getStringSerializer(),
                mRedisTemplate.getStringSerializer(),
                Collections.singletonList(key),
                lockValue, String.valueOf(expireTime));
        return LOCK_SUCCESS.equals(lockResult)?lockValue:null;
    }


    /**
     * 解锁
     *
     * @param key 锁
     * @return 是否释放成功
     */
    public boolean unlock(String key,String value){
        log.debug("redis unlock debug, start. resource:[{}].",key);
        String result = mRedisTemplate.execute(SCRIPT_UNLOCK,mRedisTemplate.getStringSerializer(),mRedisTemplate.getStringSerializer(),Collections.singletonList(key),value);
        log.debug("redis lock release status {}",result);
        return Boolean.valueOf(result);
    }
}
