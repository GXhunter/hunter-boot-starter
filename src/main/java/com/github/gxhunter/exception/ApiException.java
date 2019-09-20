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


import com.github.gxhunter.result.IResult;
import lombok.Getter;

/**
 * <p>
 * REST API 请求异常类
 * </p>
 */
@SuppressWarnings("all")
public class ApiException extends RuntimeException {

    /**
     * 错误码
     */
    @Getter
    private IResult errorCode;

    public ApiException(IResult errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }


    public ApiException(IResult errorCode, Object errorData) {
        super(errorCode.getMessage());
        this.errorCode = new IResult() {
            @Override
            public Integer getCode() {
                return errorCode.getCode();
            }

            @Override
            public String getMessage() {
                return errorCode.getMessage();
            }

            @Override
            public Object getData() {
                return errorData;
            }
        };
    }

    public ApiException(String message) {
        super(message);
    }

    public ApiException(Throwable cause) {
        super(cause);
    }

    public ApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
