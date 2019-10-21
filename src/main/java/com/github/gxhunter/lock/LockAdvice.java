package com.github.gxhunter.lock;

import com.github.gxhunter.anno.Lock;
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
 * 分布式锁切面
 * @author wanggx
 * @date 2019/5/27 15:31
 */
@Slf4j
public class LockAdvice extends AbstractPointcutAdvisor implements MethodInterceptor {

    private AbstractLockTemplate mLockTemplate;

    public LockAdvice(AbstractLockTemplate lockTemplate) {
        this.mLockTemplate = lockTemplate;
    }

    @Override
    public Pointcut getPointcut() {
        return AnnotationMatchingPointcut.forMethodAnnotation(Lock.class);
    }

    @Override
    public Advice getAdvice() {
        return this;
    }

    /**
     * 方法增强
     */
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        long id = Thread.currentThread().getId();
        long startTime = System.currentTimeMillis();
        String keyName = null;
        String lockValue = null;
        try {
            Lock lock = invocation.getMethod().getAnnotation(Lock.class);
            keyName = mLockTemplate.generateKeyName(invocation,lock);
            log.debug(keyName + "尝试获取锁...");
            while ((lockValue = mLockTemplate.lock(keyName, lock.expireTime())) == null) {
                if (startTime + lock.retryTimes() < System.currentTimeMillis()) {
//                    超时
                    throw new RedisLockException("获取锁超时,等待时间：" + lock.retryTimes() + "毫秒");
                }
                Thread.sleep(100);
            }
            log.info(id + "成功获取到锁");
            return invocation.proceed();
        } finally {
            if (StringUtils.isNoneBlank(keyName, lockValue)) {
                if (!mLockTemplate.unlock(keyName, lockValue)) {
                    log.error("redis 解锁失败,请检查是否已超时自动释放,key ： {},value ： {}.", keyName, lockValue);
                } else {
                    log.debug("redis 解锁成功,key ： {},value ： {}.", keyName, lockValue);
                }
            }
        }
    }
}
