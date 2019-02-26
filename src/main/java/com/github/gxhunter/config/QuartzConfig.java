package com.github.gxhunter.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.gxhunter.entity.QuartzInfo;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.AdaptableJobFactory;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

/**
 * @author 树荫下的天空
 * @date 2019/2/22 16:25
 */
@Configuration
@ConditionalOnBean(QuartzInfo.class)
public class QuartzConfig{
    /**
     * 配置文件
     */
    private static final String QUARTZ_PROPERTIES_NAME = "/quartz.properties";
    /**
     * 解决spring无法注入的问题
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


    /**
     * 获取工厂bean
     */
    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(Environment env,AdaptableJobFactory myJobFactory,CronTrigger[] cronTriggers,JobDetail[] jobDetails){
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        try{
            schedulerFactoryBean.setDataSource(getDatasource(env));
            schedulerFactoryBean.setQuartzProperties(quartzProperties());
            schedulerFactoryBean.setJobFactory(myJobFactory);
            schedulerFactoryBean.setTriggers(cronTriggers);
            schedulerFactoryBean.setJobDetails(jobDetails);
        }catch(IOException e){
            e.printStackTrace();
        }
        return schedulerFactoryBean;
    }

    /**
     * 数据源
     * @param env
     * @return
     */
    private DataSource getDatasource(Environment env){
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(env.getProperty("quartz.dataSource.driver-class-name"));
        dataSource.setUrl(env.getProperty("quartz.dataSource.url"));
        dataSource.setUsername(env.getProperty("quartz.dataSource.username"));
        dataSource.setPassword(env.getProperty("quartz.dataSource.password"));
        return dataSource;
    }

    /**
     * 指定quartz.properties
     */
    @Bean
    public Properties quartzProperties() throws IOException{
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource(QUARTZ_PROPERTIES_NAME));
        propertiesFactoryBean.afterPropertiesSet();
        return propertiesFactoryBean.getObject();
    }

    /**
     * 创建schedule
     */
    @Bean
    public Scheduler scheduler(SchedulerFactoryBean schedulerFactoryBean){
        return schedulerFactoryBean.getScheduler();
    }

    @Bean
    public JobFactory jobFactory(ApplicationContext applicationContext){
        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }


    class AutowiringSpringBeanJobFactory extends SpringBeanJobFactory implements ApplicationContextAware{

        private transient AutowireCapableBeanFactory beanFactory;

        @Override
        public void setApplicationContext(final ApplicationContext context){
            beanFactory = context.getAutowireCapableBeanFactory();
        }

        @Override
        protected Object createJobInstance(final TriggerFiredBundle bundle)
                throws Exception{
            final Object job = super.createJobInstance(bundle);
            beanFactory.autowireBean(job);
            return job;
        }
    }

}

