package com.github.gxhunter.lock;

import com.github.gxhunter.exception.LockException;
import com.github.gxhunter.util.ConstantValue;
import com.github.gxhunter.util.SpelPaser;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import java.util.Arrays;

/**
 * 分布式锁切面
 *
 * @author wanggx
 * @date 2019/5/27 15:31
 */
@Slf4j
public class LockAdvice extends AbstractPointcutAdvisor implements MethodInterceptor, ConstantValue.DistributeLock {

    private AbstractLockTemplate mLockTemplate;
    /**
     * spel表达式解析器
     */
    protected static final SpelPaser SPEL_PASER = new SpelPaser();

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

    @Override
    public int getOrder() {
        return AOP_ORDER;
    }

    /**
     * 方法增强
     */
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        long startTime = System.currentTimeMillis();
        String keyName = null;
        String lockValue = null;
        Lock lock = invocation.getMethod().getAnnotation(Lock.class);
        try {
            keyName = Arrays.stream(lock.keys())
                    .map(el->SPEL_PASER.parse(el, invocation.getMethod(), invocation.getArguments(), String.class))
                    .filter(StringUtils::isNotBlank)
                    .reduce((a, b)->a + SPLIT + b)
                    .map(key->PREFIX + SPLIT + key)
                    .get();

            String value = mLockTemplate.getLockValue();
            log.debug(keyName + "尝试获取锁...");
            while ((lockValue = mLockTemplate.lock(keyName, value, lock.expireTime())) == null) {
                if (startTime + lock.retryTimes() < System.currentTimeMillis()) {
//                    超时
                    throw new LockException("获取锁超时,等待时间：" + lock.retryTimes() + "毫秒");
                }
                Thread.sleep(100);
            }
            log.debug(keyName + "成功获取到锁");
            return invocation.proceed();
        } finally {
            if (! lock.delay() && StringUtils.isNoneBlank(keyName, lockValue)) {
                if (mLockTemplate.unlock(keyName, lockValue)) {
                    log.debug("redis 解锁成功,key ： {},value ： {}.", keyName, lockValue);
                } else {
                    log.error("redis 解锁失败,请检查是否已超时自动释放,key ： {},value ： {}.", keyName, lockValue);
                }
            }
        }

    }
}
