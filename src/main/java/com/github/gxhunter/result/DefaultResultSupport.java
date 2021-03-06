package com.github.gxhunter.result;

import com.github.gxhunter.controller.BaseController;
import com.github.gxhunter.entity.Result;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;

/**
 * @author wanggx
 * @date 2019/8/1 15:07
 */
@Getter
@Setter
@ConditionalOnMissingBean(ResultSupport.class)
public class DefaultResultSupport implements ResultSupport {
    /**
     * 提供一个表示“成功”的返回类到spring上下文。
     * 主要在{{@link BaseController#success()}}使用
     */
    private Result successResult = new Result<>(null,"success",1);

    /**
     * 捕获到异常时返回的result
     */
    private Result exceptionResult = new Result<>(null,null,999);;
    /**
     * 提供一个表示“失败”的返回类到spring上下文。
     */
    private Result failedResult = new Result<>(null,"faild",0);

    @Override
    public AResult success(){
        return successResult;
    }

    @Override
    public AResult faild(){
        return failedResult;
    }

    @Override
    public AResult exception(){
        return exceptionResult;
    }

}
