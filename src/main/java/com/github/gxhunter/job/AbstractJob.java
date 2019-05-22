package com.github.gxhunter.job;

import org.quartz.Job;
import org.quartz.Scheduler;

import java.util.Date;

/**
 * @author wanggx
 * @date 2019/5/22 14:25
 */
public abstract class AbstractJob implements Job{
    public static final short NEVER_TOMEOUT = 0;

    /**
     * 任务超时时间，默认{@link #NEVER_TOMEOUT} 永不超时
     * @return 时间戳
     */
    public long getTimeout(){
        return NEVER_TOMEOUT;
    }

    /**
     * 开始时间
     * @return  默认当前
     */
    public Date startTime(){
        return new Date();
    }

    /**
     * 结束定时器时间
     * @return  默认永不停止
     */
    public Date endTime(){
        return null;
    }

    /**
     * @return 组名称
     */
    public String getGroupName(){
        return Scheduler.DEFAULT_GROUP;
    }
}
