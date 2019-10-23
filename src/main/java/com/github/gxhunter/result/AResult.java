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
package com.github.gxhunter.result;

import com.github.gxhunter.entity.Result;
import lombok.extern.slf4j.Slf4j;

/**
 * @author hunter
 * @date 2019.1.4
 * @see Result
 * @see Cloneable 支持原型模式
 * 扩展自行实现此接口
 */
@Slf4j
@SuppressWarnings("all")
public abstract class AResult<T> implements Cloneable, IResult {

    public abstract void setCode(Integer code);

    public abstract void setMessage(String message);

    public abstract void setData(T data);

    @Override
    public AResult<T> clone() {
        try {
            return (AResult<T>) super.clone();
        } catch (CloneNotSupportedException e) {
            log.error("clone Result 发生异常：", e);
        }
        return null;
    }
}
