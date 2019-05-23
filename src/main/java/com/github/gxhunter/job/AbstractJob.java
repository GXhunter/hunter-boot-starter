package com.github.gxhunter.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;

/**
 * @author wanggx
 * @date 2019/5/22 14:25
 */
public abstract class AbstractJob implements Job, Runnable{
    public static final short NEVER_TOMEOUT = 0;

    @Override
    public final void execute(JobExecutionContext context) throws JobExecutionException{
        if(!before(context)){
            return;
        }
        this.run();
        after(context);
    }

    /**
     * 任务执行后
     */
    protected void after(JobExecutionContext context){

    }

    /**
     * 定时任务执行前
     *
     * @param context 上下文
     * @return 是否继续运行任务，false时不再运行定时器
     */
    protected boolean before(JobExecutionContext context){
        return true;
    }

    /**
     * 任务超时时间，默认{@link #NEVER_TOMEOUT} 永不超时
     *
     * @return 时间戳
     */
    public long getTimeout(){
        return NEVER_TOMEOUT;
    }

    /**
     * @return 组名称
     */
    public String getGroupName(){
        return Scheduler.DEFAULT_GROUP;
    }
}
