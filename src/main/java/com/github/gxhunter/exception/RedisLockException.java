package com.github.gxhunter.exception;

/**
 * @author wanggx
 * @date 2019/5/29 11:21
 */
public class RedisLockException extends RuntimeException{
    public RedisLockException(){
    }

    public RedisLockException(String message){
        super(message);
    }
}