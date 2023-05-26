package com.fosss.gulimall.cart.service;

import com.fosss.gulimall.cart.vo.CartItemVo;
import com.fosss.gulimall.cart.vo.CartVo;

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
    /**
     * 从redis中查询购物车
     */
    CartItemVo searchCartItem(Long skuId);

    /**
     * 获取购物车
     */
    CartVo getCart() throws ExecutionException, InterruptedException;
    /**
     * 选中购物项
     */
    void checkItem(Long skuId, Integer check);
    /**
     * 修改数量
     */
    void countItem(Long skuId, Integer num);
    /**
     * 删除购物项
     */
    void deleteItem(Long skuId);
}
