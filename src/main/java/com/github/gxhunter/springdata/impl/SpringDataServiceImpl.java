package com.github.gxhunter.springdata.impl;

import com.github.gxhunter.exception.ApiException;
import com.github.gxhunter.springdata.ISpringDataService;
import com.github.gxhunter.util.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.lang.reflect.Field;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * @param <T> 持久层
 * @param <E> 实体类
 * @author 树荫下的天空
 */
public class SpringDataServiceImpl<T extends PagingAndSortingRepository<E, Long>,E> implements ISpringDataService<E>{
    /**
     * 持久层
     */
    @Autowired
    protected T mRepository;
    private E mEntity;

    /**
     * 查询全部记录
     *
     * @return
     */
    @Override
    public List<E> list(){
        return (List<E>) mRepository.findAll();
    }

    /**
     * 根据主键查询实体
     *
     * @param id
     * @return
     */
    @Override
    public E getById(Long id){
        try{
            return mRepository.findById(id).orElse(null);
        }catch(NoSuchElementException e){
            return null;
        }
    }

    /**
     * 增加
     *
     * @param entity
     */
    @Override
    public E save(E entity){
        Field field = getIdField(entity);
        try{
            field.set(entity,IdWorker.getId());
        }catch(IllegalAccessException e){
            e.printStackTrace();
        }
        return mRepository.save(entity);
    }

    @Override
    public Iterable<E> saveBatch(Iterable<E> entities){
        return mRepository.saveAll(entities);
    }

    @Override
    public E updateById(Long id,E entity){
        Field field = getIdField(entity);
        try{
            field.set(entity,id);
        }catch(IllegalAccessException e){
            e.printStackTrace();
        }
        return mRepository.save(entity);
    }

    @Override
    public E saveOrUpdate(E entity){
        try{
            Field field = getIdField(entity);
            Long id = (Long) field.get(entity);
            if(id == null){
                return save(entity);
            }else {
                return updateById(id,entity);
            }
        }catch(IllegalAccessException e){
            e.printStackTrace();
        }
        return mRepository.save(entity);
    }

    /**
     * 删除
     *
     * @param id
     */
    @Override
    public void removeById(Long id){
        mRepository.deleteById(id);
    }

    @Override
    public Page<E> page(Pageable page){
        return mRepository.findAll(page);
    }

    @Override
    public Page<E> page(Integer pageNum,Integer pageSize){
        PageRequest request = PageRequest.of(pageNum,pageSize);
        return mRepository.findAll(request);
    }

    @Override
    public void removeAll(){
        mRepository.deleteAll();
    }

    @Override
    public void removeAll(Iterable<E> iterable){
        mRepository.deleteAll(iterable);
    }


    @Override
    public boolean existsById(Long id){
        return mRepository.existsById(id);
    }

    @Override
    public long count(){
        return mRepository.count();
    }

    /**
     * 获取主键字段
     * @param entity
     * @return
     */
    private Field getIdField(E entity){
        for(Field field : entity.getClass().getFields()){
            if(field.getAnnotation(Id.class)!=null){
                return field;
            }
        }
        throw new ApiException("你没有指定id字段，请添加@Id注解");
    }

}
