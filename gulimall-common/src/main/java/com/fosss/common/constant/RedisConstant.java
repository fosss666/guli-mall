package com.fosss.common.constant;

import java.time.LocalDateTime;
import java.util.Random;

/**
 * @author: fosss
 * Date: 2023/3/1
 * Time: 15:00
 * Description:
 */
public class RedisConstant {
    public static final String PRODUCT_CATEGORY_KEY = "categoryJson";

    //空数据缓存时间   300s
    public static final long CACHE_NULL_TIME = 300;
    //其他数据缓存时间 300s*12*24  =24h  +随机数
    public static final long CACHE_OTHER_TIME = 300 * 12 * 24 + new Random().nextInt(1000);
}



















