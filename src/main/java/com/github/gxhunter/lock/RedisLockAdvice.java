package com.github.gxhunter.lock;

import com.github.gxhunter.anno.RedisLock;
import com.github.gxhunter.exception.RedisLockException;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;
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
        log.debug("redis lock running");
        long startTime = System.currentTimeMillis();
        String keyName = null;
        String lockValue = null;
        try{
            RedisLock redisLock = invocation.getMethod().getAnnotation(RedisLock.class);
            keyName = redisDistributionLock.generateKeyName(invocation,redisLock);
            while((lockValue = redisDistributionLock.lock(keyName,redisLock.expireTime())) == null){
                if(startTime + redisLock.retryTimes() < System.currentTimeMillis()){
//                    超时
                    throw new RedisLockException("获取锁超时,等待时间："+redisLock.retryTimes()+"毫秒");
                }
                Thread.sleep(50);
            }
            log.debug("lock has bean got.method process");
            return invocation.proceed();
        }finally{
            if(StringUtils.isNoneBlank(keyName,lockValue)){
                if(!redisDistributionLock.unlock(keyName,lockValue)){
                    log.error("redis unlock failed,the key is {},value is {}.",keyName,lockValue);
                }
            }
        }
    }
}
