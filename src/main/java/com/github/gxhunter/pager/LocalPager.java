package com.github.gxhunter.pager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.gxhunter.util.Assert;

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
    private int pageSize;
    private int current;

    private int startIndex;
    private int endIndex;

    public LocalPager(int current, int pageSize) {
        Assert.isTrue(current > 0 && pageSize > 0);
        this.pageSize = pageSize;
        this.current = current;
        this.startIndex = pageSize * (current - 1);
        this.endIndex = startIndex + pageSize;
    }


    @Override
    public List<T> getRecords() {
        if (endIndex > records.size() - 1) {
            endIndex = records.size() - 1;
        }
        if (startIndex >= endIndex) {
            startIndex = endIndex;
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
        return records.size();
    }

    @Override
    public IPage<T> setTotal(long total) {
        throw new IllegalStateException("不能自定义长度");
    }

    @Override
    public long getSize() {
        return Math.min(this.pageSize, this.records.size());
    }

    @Override
    public IPage<T> setSize(long size) {
        throw new IllegalStateException("禁止指定");
    }

    @Override
    public long getCurrent() {
        return this.current;
    }

    @Override
    public IPage<T> setCurrent(long current) {
        throw new IllegalStateException("禁止指定");
    }
}
