package com.fosss.gulimall.authserver.vo;

import lombok.Data;

/**
 * 社交用户信息
 **/

@Data
public class SocialUser {

    /**
     * 令牌
     */
    private String access_token;

    private String token_type;
    /**
     * 过期时间
     */
    private long expires_in;

    private String refresh_token;

    private String scope;

    private String created_at;
    /**
     * 用户id
     */
    private String uid;

}
