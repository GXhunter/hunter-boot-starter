package com.github.gxhunter.service.impl;

import com.github.gxhunter.entity.BaseMongoEntity;
import com.github.gxhunter.service.IMongoService;
import com.github.gxhunter.util.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * @param <T>  持久层
 * @param <E>  实体类
 * @author 树荫下的天空
 */
public class MongoServiceImpl<T extends MongoRepository<E,Long>,E extends BaseMongoEntity> implements IMongoService<E>{
    @Autowired
    protected T mDao;

    /**
     * 查询全部记录
     *
     * @return
     */
    @Override
    public List<E> findAll(){
        return mDao.findAll();
    }

    /**
     * 根据主键查询实体
     *
     * @param id
     * @return
     */
    @Override
    public E findById(Long id){
        try{
            return mDao.findById(id).get();
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
    public E add(E entity){
        entity.setId(IdWorker.getId());
        return mDao.save(entity);
    }

    @Override
    public E update(Long id,E entity){
        entity.setId(id);
        return mDao.save(entity);
    }

    @Override
    public E saveOrUpdate(E entity){
        return mDao.save(entity);
    }

    /**
     * 删除
     *
     * @param id
     */
    @Override
    public void deleteById(Long id){
        mDao.deleteById(id);
    }

    @Override
    public Page<E> page(Pageable page){
        return mDao.findAll(page);
    }

    @Override
    public void deleteAll(){
        mDao.deleteAll();
    }


}
