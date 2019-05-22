package com.github.gxhunter.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.gxhunter.entity.QuartzInfo;
import com.github.gxhunter.job.AbstractJob;
import com.github.gxhunter.job.TimerJob;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.quartz.*;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.quartz.QuartzAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.AdaptableJobFactory;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

/**
 * @author 树荫下的天空
 * @date 2019/2/22 16:25
 */
@Configuration
@AutoConfigureAfter(QuartzAutoConfiguration.class)
@ConditionalOnBean(Scheduler.class)
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

    @Bean
    public Object jobInit(ApplicationContext context,Scheduler scheduler){
        Map<String, Object> jobMap = context.getBeansWithAnnotation(TimerJob.class);
        jobMap.forEach((beanName,job) -> {
            if(!(job instanceof AbstractJob)){
                throw new IllegalArgumentException(job.getClass().getName() + " should extends "+AbstractJob.class.getName());
            }
            AbstractJob abstractJob = (AbstractJob) job;

            Class<? extends AbstractJob> jobClass = abstractJob.getClass();
            TimerJob annotation = jobClass.getAnnotation(TimerJob.class);
            String groupName = StringUtils.isBlank(abstractJob.getGroupName()) ? Scheduler.DEFAULT_GROUP : abstractJob.getGroupName();

            CronTrigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(jobClass.getName(),groupName)
                    .withSchedule(CronScheduleBuilder.cronSchedule(annotation.cron()))
                    .startAt(abstractJob.startTime())
                    .endAt(abstractJob.endTime())
                    .build();

            JobDetail jobDetail = JobBuilder.newJob(jobClass)
                    .withIdentity(jobClass.getName(),groupName)
                    .build();

            try{
                scheduler.scheduleJob(jobDetail,trigger);
            }catch(SchedulerException e){
                throw new RuntimeException(e);
            }
        });
        return null;
    }


}

