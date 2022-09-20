package com.itheima.reggie.common;

/**
 * 1. @ClassDescription:
 * 2. @author: ZhangL
 * 3. @date: 2022年09月14日 21:59
 */
public class CustomException extends RuntimeException{
    public CustomException(String message){
        super(message);
    }
}
