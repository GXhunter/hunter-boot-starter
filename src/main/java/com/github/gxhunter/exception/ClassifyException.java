/*
 * Copyright (c) 2011-2020, hubin (jobob@qq.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.github.gxhunter.exception;


import com.github.gxhunter.enums.IResponseCode;
import lombok.Getter;

/**
 * <p>
 * 分类异常 Controller加上{@link com.github.gxhunter.controller.BaseController.IfException}后全部自动归类到此异常。
 * </p>
 */
@SuppressWarnings("all")
public class ClassifyException extends RuntimeException {

    /**
     * 错误码
     */
    @Getter
    private IResponseCode exceptionInfo;

    /**
     * 捕获到的原始异常类型
     */
    @Getter
    private Class<? extends Exception> exceptionClass;

    public ClassifyException(IResponseCode exceptionInfo) {
        super(exceptionInfo.getMessage());
        this.exceptionInfo = exceptionInfo;
    }


    public ClassifyException(IResponseCode exceptionInfo,Class<? extends Exception> exceptionClass) {
        super(exceptionInfo.getMessage());
        this.exceptionInfo = exceptionInfo;
        this.exceptionClass = exceptionClass;
    }

    public ClassifyException(String message) {
        super(message);
    }

    public ClassifyException(Throwable cause) {
        super(cause);
    }

    public ClassifyException(String message,Throwable cause) {
        super(message, cause);
    }
}
