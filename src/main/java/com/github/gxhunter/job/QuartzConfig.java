package com.github.gxhunter.job;

import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.impl.triggers.AbstractTrigger;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.quartz.QuartzAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.AdaptableJobFactory;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 树荫下的天空
 * @date 2019/2/22 16:25
 */
@Configuration
@ConditionalOnClass({ Scheduler.class, SchedulerFactoryBean.class,
        PlatformTransactionManager.class,QuartzAutoConfiguration.class })
@AutoConfigureAfter(QuartzAutoConfiguration.class)
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
        List<CronTriggerImpl> cronTriggers = new ArrayList<>();

        Map<String, Object> jobMap = context.getBeansWithAnnotation(QuartzJob.class);
        jobMap.forEach((beanName,job) -> {
            if(!(job instanceof AbstractJob)){
                throw new IllegalArgumentException(job.getClass().getName() + " should extends " + AbstractJob.class.getName());
            }
            AbstractJob abstractJob = (AbstractJob) job;
            Class<? extends AbstractJob> jobClass = abstractJob.getClass();
            QuartzJob quartzJob = jobClass.getAnnotation(QuartzJob.class);
            String groupName = abstractJob.getGroupName();

            JobKey jobKey = new JobKey(beanName,groupName);
            JobDetail jobDetail = createJobDetail(jobClass,jobKey);
            CronTriggerImpl trigger = createTrigger(quartzJob.cron(),jobKey,jobDetail);
            cronTriggers.add(trigger);

        });
        return cronTriggers.toArray(new CronTriggerImpl[0]);
    }


    /**
     * 创建jobTrigger
     *
     * @param cronExpression cron表达式
     * @param jobKey         默认trigger的jobKey和jobDetail的jobKey一样
     * @return
     * @throws ParseException
     */
    private CronTriggerImpl createTrigger(String cronExpression,JobKey jobKey,JobDetail jobDetail){
        CronTriggerFactoryBean triggerFactoryBean = new CronTriggerFactoryBean();
        triggerFactoryBean.setJobDetail(jobDetail);
        triggerFactoryBean.setCronExpression(cronExpression);
        triggerFactoryBean.setName(jobKey.getName());
        triggerFactoryBean.setGroup(jobKey.getGroup());
        try{
            triggerFactoryBean.afterPropertiesSet();
        }catch(ParseException e){
            throw new RuntimeException(e);
        }
        return (CronTriggerImpl) triggerFactoryBean.getObject();
    }

    /**
     * 通过job创建jobDetail
     *
     * @param jobClass      job类
     * @param jobKey
     * @return
     */
    private JobDetail createJobDetail(Class<? extends AbstractJob> jobClass,JobKey jobKey){
        JobDetailFactoryBean detailFactoryBean = new JobDetailFactoryBean();
        detailFactoryBean.setDurability(true);
        detailFactoryBean.setRequestsRecovery(true);
        detailFactoryBean.setJobClass(jobClass);
        detailFactoryBean.setName(jobKey.getName());
        detailFactoryBean.setGroup(jobKey.getGroup());
        detailFactoryBean.afterPropertiesSet();
        return detailFactoryBean.getObject();
    }


    /**
     * 添加该方法的目的在于一个使用场景。如果代码中删除了不需要的定时任务，但是数据库中不会删除掉，会导致之前
     * 的定时任务一直在运行，如果把定时任务依赖的类删除了，就会导致报错，找不到目标。所以配置动态删除任务
     * @param triggers
     * @param scheduler
     * @return
     */
    public String fulsh(@Qualifier("triggers") CronTriggerImpl[] triggers,Scheduler scheduler){
        try{
            // 最新配置的任务
            List<String> newTriNames = Arrays.stream(triggers).map(AbstractTrigger::getName).collect(Collectors.toList());

            // 现有数据库中已有的任务
            Set<TriggerKey> myGroupTriggers = scheduler.getTriggerKeys(GroupMatcher.anyTriggerGroup());
            if(null == myGroupTriggers || myGroupTriggers.size() == 0){
                return "myGroupTriggers is null";
            }

            if(newTriNames.size() > 0){
                for(TriggerKey triggerKey : myGroupTriggers){
                    String dbTriggerName = triggerKey.getName();
                    if(!newTriNames.contains(dbTriggerName)){
                        // 暂停 触发器
                        scheduler.pauseTrigger(triggerKey);
                        Trigger trigger = scheduler.getTrigger(triggerKey);
                        JobKey jobKey = null;
                        if(null != trigger){
                            jobKey = trigger.getJobKey();
                        }
                        // 停止触发器
                        scheduler.pauseTrigger(triggerKey);
                        // 注销 触发器
                        scheduler.unscheduleJob(triggerKey);
                        if(null != jobKey){
                            // 暂停任务
                            scheduler.pauseJob(jobKey);
                            // 删除任务
                            scheduler.deleteJob(jobKey);
                        }
                    }
                }
            }
            // 重要，如果不恢复所有，会导致无法使用
            scheduler.resumeAll();
        }catch(Exception e){
            e.printStackTrace();
            return "Exception:" + e.getMessage();
        }
        return "success";
    }

}

