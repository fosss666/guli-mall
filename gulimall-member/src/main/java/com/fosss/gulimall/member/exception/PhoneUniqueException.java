package com.fosss.gulimall.member.exception;

/**
 * @author: fosss
 * Date: 2023/5/4
 * Time: 18:56
 * Description: 手机号（邮箱地址）不唯一异常
 */
public class PhoneUniqueException extends RuntimeException {
    public PhoneUniqueException(String message) {
        super(message);
    }
}
