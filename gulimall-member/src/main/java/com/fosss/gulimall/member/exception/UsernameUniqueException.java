package com.fosss.gulimall.member.exception;

/**
 * @author: fosss
 * Date: 2023/5/4
 * Time: 18:55
 * Description: 用户名不唯一异常
 */
public class UsernameUniqueException extends RuntimeException{
    public UsernameUniqueException(String message){
        super(message);
    }
}
