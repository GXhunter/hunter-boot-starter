package com.github.gxhunter.pager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * 本地分页
 *
 * @author wanggx
 * @date 2020-04-10 15:16
 **/
public class LocalPager<T> implements IPage<T> {
    /**
     * 总得记录
     */
    private List<T> records;
    private final Integer pageSize;
    private final Integer current;

    private int startIndex;
    private int endIndex;

    public LocalPager(Integer current, Integer pageSize) {
        this.pageSize = pageSize;
        this.current = current;
        if (current != null && pageSize != null) {
            this.startIndex = pageSize * (current - 1);
            this.endIndex = startIndex + pageSize;
        }
    }


    @Override
    public List<OrderItem> orders() {
        return null;
    }

    @Override
    public List<T> getRecords() {
        if (CollectionUtils.isEmpty(records)) {
            return null;
        }
        if (this.current == null || this.pageSize == null) {
            return records;
        }
        if (endIndex > records.size() - 1) {
            endIndex = Math.max(records.size() - 1, 0);
        }
        if (startIndex >= endIndex) {
            return null;
        }
        return records.subList(startIndex, endIndex);
    }

    @Override
    public IPage<T> setRecords(List<T> records) {
        this.records = records;
        return this;
    }

    @Override
    public long getTotal() {
        return records==null?0:records.size();
    }

    @Override
    public IPage<T> setTotal(long total) {
        throw new IllegalStateException("不能自定义长度");
    }

    @Override
    public long getSize() {
        return pageSize == null ? this.records.size() : Math.min(this.pageSize, this.records.size());
    }

    @Override
    public IPage<T> setSize(long size) {
        throw new IllegalStateException("禁止指定");
    }

    @Override
    public long getCurrent() {
        return this.current != null ? this.current : 1;
    }

    @Override
    public IPage<T> setCurrent(long current) {
        throw new IllegalStateException("禁止指定");
    }
}
