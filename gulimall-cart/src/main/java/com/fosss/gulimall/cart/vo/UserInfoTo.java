package com.fosss.gulimall.cart.vo;

import lombok.Data;
import lombok.ToString;

/**
 * @author: fosss
 * Date: 2023/5/22
 * Time: 21:26
 * Description:
 */
@ToString
@Data
public class UserInfoTo {
    private Long userId;
    private String userKey;
    //标记是否有临时用户
    private Boolean isTempUser = false;
}
