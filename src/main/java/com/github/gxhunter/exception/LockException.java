package com.github.gxhunter.exception;

/**
 * @author wanggx
 * @date 2019/5/29 11:21
 */
public class LockException extends RuntimeException{
    public LockException(){
    }

    public LockException(String message){
        super(message);
    }
}