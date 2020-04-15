package com.github.gxhunter.pager;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author wanggx
 * @date 2020-04-15 09:25
 **/
@Data
public class LocalOrder<T> {
    private BiFunction<T, T, Integer> sort;

    public LocalOrder(Function<T, String> field, boolean isAsc) {
        this.sort = (t, t2) -> isAsc ? StringUtils.compare(field.apply(t),field.apply(t2)):StringUtils.compare(field.apply(t2),field.apply(t));
    }
}
