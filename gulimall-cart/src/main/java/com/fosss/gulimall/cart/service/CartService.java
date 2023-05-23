package com.fosss.gulimall.cart.service;

import com.fosss.gulimall.cart.vo.CartItemVo;

import java.util.concurrent.ExecutionException;

/**
 * @author: fosss
 * Date: 2023/5/22
 * Time: 21:19
 * Description:
 */
public interface CartService {
    /**
     * 将商品添加到购物车
     */
    CartItemVo addToCart(Long skuId, Integer num) throws ExecutionException, InterruptedException;
}
