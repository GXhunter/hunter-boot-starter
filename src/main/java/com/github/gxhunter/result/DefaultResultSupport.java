package com.github.gxhunter.result;

import com.github.gxhunter.controller.BaseController;
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
    private final Result successResult = new Result<>(null,"success",1);

    /**
     * 捕获到异常时返回的result
     */
    private final Result exceptionResult = new Result<>(null,null,999);;
    /**
     * 提供一个表示“失败”的返回类到spring上下文。
     */
    private final Result failedResult = new Result<>(null,"faild",0);

    @Override
    public Result success(){
        return successResult;
    }

    @Override
    public Result faild(){
        return failedResult;
    }

}
