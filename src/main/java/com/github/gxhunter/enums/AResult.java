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
package com.github.gxhunter.enums;

import lombok.extern.slf4j.Slf4j;

/**
 * @author hunter
 * @date 2019.1.4
 * @see com.github.gxhunter.vo.Result
 * @see Cloneable 支持原型模式
 * 扩展自行实现此接口
 */
@Slf4j
@SuppressWarnings("all")
public abstract class AResult<T> implements Cloneable{
    /**
     * 返回码
     *
     * @return json序列化可在yml配置
     * <pre>
     * hunter:
     *   spring:
     *     result:
     *       status-key: "status"
     * <pre/>
     */
    public abstract Integer getCode();

    /**
     * 错误描述
     *
     * @return json序列化可在yml配置
     * <p>
     * hunter:
     * spring:
     * result:
     * message-key: "msg"
     * <p/>
     */
    public abstract String getMessage();

    public abstract T getData();


    public abstract void setCode(Integer code);

    public abstract void setMessage(String message);

    public abstract void setData(T data);

    @Override
    public AResult<T> clone(){
        try{
            return (AResult<T>) super.clone();
        }catch(CloneNotSupportedException e){
            log.error("clone Result 发生异常：",e);
        }
        return null;
    }
}
