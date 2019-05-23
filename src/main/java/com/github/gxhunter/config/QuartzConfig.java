package com.github.gxhunter.config;

import com.github.gxhunter.job.AbstractJob;
import com.github.gxhunter.job.TimerJob;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.quartz.*;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.quartz.QuartzAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.AdaptableJobFactory;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * @author 树荫下的天空
 * @date 2019/2/22 16:25
 */
@Configuration
@ConditionalOnClass(QuartzAutoConfiguration.class)
public class QuartzConfig{

    /**
     * 解决spring无法注入的问题
     *
     * @param capableBeanFactory
     * @return
     */
    @Bean
    public AdaptableJobFactory myJobFactory(AutowireCapableBeanFactory capableBeanFactory){
        return new AdaptableJobFactory(){
            @Override
            protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception{
                Object jobInstance = super.createJobInstance(bundle);
                //这一步解决不能spring注入bean的问题
                capableBeanFactory.autowireBean(jobInstance);
                return jobInstance;
            }
        };
    }

    @Bean(name = "triggers")
    public CronTriggerImpl[] jobInit(ApplicationContext context){
        List<CronTriggerImpl> cronTriggers = Lists.newArrayList();

        Map<String, Object> jobMap = context.getBeansWithAnnotation(TimerJob.class);
        jobMap.forEach((beanName,job) -> {
            if(!(job instanceof AbstractJob)){
                throw new IllegalArgumentException(job.getClass().getName() + " should extends " + AbstractJob.class.getName());
            }
            AbstractJob abstractJob = (AbstractJob) job;
            Class<? extends AbstractJob> jobClass = abstractJob.getClass();
            TimerJob timerJob = jobClass.getAnnotation(TimerJob.class);
            String groupName = StringUtils.isBlank(abstractJob.getGroupName()) ? Scheduler.DEFAULT_GROUP : abstractJob.getGroupName();

            JobKey jobKey = new JobKey(beanName,groupName);
            JobDetail jobDetail = createJobDetail(jobClass,jobKey);
            CronTriggerImpl trigger = createTrigger(jobClass,timerJob.cron(),jobKey,jobDetail);
            cronTriggers.add(trigger);

        });
        return cronTriggers.toArray(new CronTriggerImpl[cronTriggers.size()]);
    }


    /**
     * 创建jobTrigger
     *
     * @param t              job类
     * @param cronExpression cron表达式
     * @param jobKey         默认trigger的jobKey和jobDetail的jobKey一样
     * @return
     * @throws ParseException
     */
    private CronTriggerImpl createTrigger(Class<? extends AbstractJob> t,String cronExpression,JobKey jobKey,JobDetail jobDetail){
        CronTriggerFactoryBean c = new CronTriggerFactoryBean();
        c.setJobDetail(jobDetail);
        c.setCronExpression(cronExpression);
        c.setName(jobKey.getName());
        c.setGroup(jobKey.getGroup());
        try{
            c.afterPropertiesSet();
        }catch(ParseException e){
            throw new RuntimeException(e);
        }
        return (CronTriggerImpl) c.getObject();
    }

    /**
     * 通过job创建jobDetail
     *
     * @param c      job类
     * @param jobKey
     * @return
     */
    private JobDetail createJobDetail(Class<? extends AbstractJob> c,JobKey jobKey){
        JobDetailFactoryBean d = new JobDetailFactoryBean();
        d.setDurability(true);
        d.setRequestsRecovery(true);
        d.setJobClass(c);
        d.setName(jobKey.getName());
        d.setGroup(jobKey.getGroup());
        d.afterPropertiesSet();
        return d.getObject();
    }

}

