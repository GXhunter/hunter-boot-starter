package com.github.gxhunter.pager;

import lombok.Data;

import java.util.function.BiFunction;

/**
 * @author wanggx
 * @date 2020-04-15 09:25
 **/
@Data
public class LocalOrder <T>{
    private BiFunction<T,T,Integer> sort;

}
