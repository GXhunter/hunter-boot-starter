package com.github.gxhunter.jackson;

import com.github.gxhunter.controller.BaseController;
import com.github.gxhunter.enums.IResult;
import com.github.gxhunter.vo.Result;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * @author wanggx
 * @date 2019/8/1 15:07
 */
@Getter
@Setter
@ConditionalOnMissingBean(IResultAware.class)
public class ResultAware implements IResultAware{
    @Autowired
    private IResult mResult;
    /**
     * 提供一个表示“成功”的返回类到spring上下文。
     * 主要在{{@link BaseController#success()}}使用
     */
    private Result successResult = new Result<>(null,"success",1);

    /**
     * 捕获到异常时返回的result
     */
    private Result exceptionResult;
    /**
     * 提供一个表示“失败”的返回类到spring上下文。
     * 主要在{{@link BaseController#faild()}}和异常处理相关使用
     *
     * @see com.github.gxhunter.controller.ExceptionResolveAdvice
     * @see com.github.gxhunter.controller.BaseController.ExceptionList
     * @see com.github.gxhunter.controller.BaseController.IfException
     */
    private Result failedResult = new Result<>(null,"faild",0);

    @Override
    public IResult success(){
        return successResult;
    }

    @Override
    public IResult faild(){
        return failedResult;
    }

    @Override
    public IResult exception(){
        return exceptionResult != null ? exceptionResult : failedResult;
    }

}
