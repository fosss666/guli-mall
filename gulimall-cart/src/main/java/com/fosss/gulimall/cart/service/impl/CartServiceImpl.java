package com.fosss.gulimall.cart.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.fosss.common.constant.CartConstant;
import com.fosss.common.utils.R;
import com.fosss.gulimall.cart.feign.ProductFeignService;
import com.fosss.gulimall.cart.interceptor.GulimallInterceptor;
import com.fosss.gulimall.cart.service.CartService;
import com.fosss.gulimall.cart.vo.CartItemVo;
import com.fosss.gulimall.cart.vo.SkuInfoVo;
import com.fosss.gulimall.cart.vo.UserInfoTo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author: fosss
 * Date: 2023/5/22
 * Time: 21:19
 * Description:
 */
@Slf4j
@Service
public class CartServiceImpl implements CartService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private ThreadPoolExecutor threadPoolExecutor;
    @Resource
    private ProductFeignService productFeignService;

    /**
     * 将商品添加到购物车
     * 使用多线程提高性能
     */
    @Override
    public CartItemVo addToCart(Long skuId, Integer num) throws ExecutionException, InterruptedException {
        CartItemVo cartItemVo = new CartItemVo();
        //获取统一redis
        BoundValueOperations<String, String> cartRedis = cartRedis();

        //远程根据skuId获取sku信息
        CompletableFuture<Void> skuInfo = CompletableFuture.runAsync(() -> {
            R info = productFeignService.info(skuId);
            SkuInfoVo data = info.getData("skuInfo", new TypeReference<SkuInfoVo>() {
            });
            //将data中的信息复制到返回对象中
            cartItemVo.setSkuId(skuId);
            cartItemVo.setCheck(true);
            cartItemVo.setCount(num);
            cartItemVo.setImage(data.getSkuDefaultImg());
            cartItemVo.setPrice(data.getPrice());
            cartItemVo.setTitle(data.getSkuTitle());
        }, threadPoolExecutor);

        //远程获取attr
        CompletableFuture<Void> attr = CompletableFuture.runAsync(() -> {
            List<String> attrsAsStringList = productFeignService.getAttrsAsStringList(skuId);
            cartItemVo.setSkuAttrValues(attrsAsStringList);
        });

        //在上面两个线程全部执行完毕后，将结果对象存储到redis中并返回
        CompletableFuture.allOf(skuInfo, attr).get();
        //转为json
        String string = JSON.toJSONString(cartItemVo);
        cartRedis.set(string);
        return cartItemVo;
    }

    /**
     * 统一redis的Hash存储
     */
    private BoundValueOperations<String, String> cartRedis() {
        //从ThreadLocal中获取用户信息
        UserInfoTo userInfoTo = GulimallInterceptor.threadLocal.get();
        //判断是否登录
        String userKey = "";
        if (userInfoTo.getUserId() != null) {
            //登录了
            userKey = CartConstant.CART_PREFIX + userInfoTo.getUserId();
        } else {
            userKey = CartConstant.CART_PREFIX + userInfoTo.getUserKey();
        }
        BoundValueOperations<String, String> cartOps = stringRedisTemplate.boundValueOps(userKey);
        return cartOps;
    }
}






















