package com.github.gxhunter.lock;

import com.github.gxhunter.anno.RedisLock;
import com.github.gxhunter.exception.RedisLockException;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;

/**
 * @author wanggx
 * @date 2019/5/27 15:31
 */
@Slf4j
public class RedisLockAdvice extends AbstractPointcutAdvisor implements MethodInterceptor{
    private RedisDistributionLock redisDistributionLock;

    public RedisLockAdvice(RedisDistributionLock redisDistributionLock){
        this.redisDistributionLock = redisDistributionLock;
    }

    @Override
    public Pointcut getPointcut(){
        return AnnotationMatchingPointcut.forMethodAnnotation(RedisLock.class);
    }

    @Override
    public Advice getAdvice(){
        return this;
    }

    /**
     * 方法增强
     *
     * @param invocation
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable{
        long startTime = System.currentTimeMillis();
        Boolean lock = false;
        String keyName = null;
        try{
            RedisLock lock4j = invocation.getMethod().getAnnotation(RedisLock.class);
            keyName = redisDistributionLock.generateKeyName(invocation,lock4j);
            while(!(lock = redisDistributionLock.lock(keyName,RedisDistributionLock.UNIQUELY_IDENTIFIES,lock4j.expireTime()))){
                if(startTime + lock4j.retryTimes() < System.currentTimeMillis()){
//                    超时
                    throw new RedisLockException("获取锁超时");
                }
                Thread.sleep(50);
            }
            return invocation.proceed();
        }finally{
            if(lock && keyName != null){
                if(!redisDistributionLock.unlock(keyName,RedisDistributionLock.UNIQUELY_IDENTIFIES)){
                    log.error("redis unlock failed,the key is {},value is {}.",keyName,RedisDistributionLock.UNIQUELY_IDENTIFIES);
                }
            }
        }
    }
}
