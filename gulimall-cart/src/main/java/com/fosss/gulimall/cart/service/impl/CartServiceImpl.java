package com.fosss.gulimall.cart.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.fosss.common.constant.CartConstant;
import com.fosss.common.utils.R;
import com.fosss.gulimall.cart.feign.ProductFeignService;
import com.fosss.gulimall.cart.interceptor.GulimallInterceptor;
import com.fosss.gulimall.cart.service.CartService;
import com.fosss.gulimall.cart.vo.CartItemVo;
import com.fosss.gulimall.cart.vo.CartVo;
import com.fosss.gulimall.cart.vo.SkuInfoVo;
import com.fosss.gulimall.cart.vo.UserInfoTo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

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
     * 删除购物项
     */
    @Override
    public void deleteItem(Long skuId) {
        BoundHashOperations<String, Object, Object> cartRedis = cartRedis();
        cartRedis.delete(skuId.toString());
    }

    /**
     * 修改数量
     */
    @Override
    public void countItem(Long skuId, Integer num) {
        CartItemVo cartItemVo = searchCartItem(skuId);
        cartItemVo.setCount(num);
        BoundHashOperations<String, Object, Object> cartRedis = cartRedis();
        cartRedis.put(skuId.toString(), JSON.toJSONString(cartItemVo));

    }

    /**
     * 选中购物项
     */
    @Override
    public void checkItem(Long skuId, Integer check) {
        //查询当前购物项
        CartItemVo cartItemVo = searchCartItem(skuId);
        //修改选中状态
        cartItemVo.setCheck(check == 1);
        //转为json保存到redis
        String string = JSON.toJSONString(cartItemVo);
        BoundHashOperations<String, Object, Object> cartRedis = cartRedis();
        cartRedis.put(skuId.toString(), string);
    }

    /**
     * 获取购物车
     * 根据是否登录分别获取
     */
    @Override
    public CartVo getCart() throws ExecutionException, InterruptedException {
        UserInfoTo userInfoTo = GulimallInterceptor.threadLocal.get();
        Long userId = userInfoTo.getUserId();

        CartVo cartVo = new CartVo();
        //从临时购物车中获取数据
        String tempUserKey = CartConstant.CART_PREFIX + userInfoTo.getUserKey();
        List<CartItemVo> cartItems = getCartItems(tempUserKey);
        if (userId == null) {
            //没有登录，则从临时购物车中获取
            cartVo.setItems(cartItems);
        } else {
            //已经登录了，则从购物车中获取，并将临时购物车合并到购物车，删除临时购物车
            //将临时购物车添加到购物车
            for (CartItemVo cartItem : cartItems) {
                addToCart(cartItem.getSkuId(), cartItem.getCount());
            }
            //获取合并的数据
            String userKey = CartConstant.CART_PREFIX + userInfoTo.getUserId();
            List<CartItemVo> cartItems1 = getCartItems(userKey);
            cartVo.setItems(cartItems1);

        }
        return cartVo;
    }

    /**
     * 获取redis中的购物项
     */
    private List<CartItemVo> getCartItems(String userKey) {
        BoundHashOperations<String, Object, Object> boundHashOps = stringRedisTemplate.boundHashOps(userKey);
        List<Object> values = boundHashOps.values();
        List<CartItemVo> cartItems = values.stream().map((obj) -> {
            CartItemVo cartItemVo = JSON.parseObject((String) obj, CartItemVo.class);
            return cartItemVo;
        }).collect(Collectors.toList());
        return cartItems;
    }


    /**
     * 将商品添加到购物车
     * 使用多线程提高性能
     */
    @Override
    public CartItemVo addToCart(Long skuId, Integer num) throws ExecutionException, InterruptedException {
        //获取统一redis
        BoundHashOperations<String, Object, Object> cartRedis = cartRedis();

        /**
         * 根据购物车中是否有该sku进行分别处理，如果有，则只改数量即可，没有则需要添加
         */
        String cart = (String) cartRedis.get(skuId.toString());
        if (!StringUtils.isEmpty(cart)) {
            //有该商品
            //获取原数据修改数量
            CartItemVo cartItemVo = JSON.parseObject(cart, CartItemVo.class);
            cartItemVo.setCount(cartItemVo.getCount() + num);
            return cartItemVo;
        } else {
            //没有该商品，需要添加
            CartItemVo cartItemVo = new CartItemVo();

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
            },threadPoolExecutor);

            //在上面两个线程全部执行完毕后，将结果对象存储到redis中并返回
            CompletableFuture.allOf(skuInfo, attr).get();
            //转为json
            String string = JSON.toJSONString(cartItemVo);
            cartRedis.put(skuId.toString(), string);
            return cartItemVo;
        }

    }

    /**
     * 从redis中查询购物项
     */
    @Override
    public CartItemVo searchCartItem(Long skuId) {
        BoundHashOperations<String, Object, Object> cartRedis = cartRedis();
        String cart = (String) cartRedis.get(skuId.toString());
        CartItemVo cartItemVo = JSON.parseObject(cart, CartItemVo.class);
        return cartItemVo;
    }

    /**
     * 统一redis的Hash存储
     */
    private BoundHashOperations<String, Object, Object> cartRedis() {
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
        BoundHashOperations<String, Object, Object> cartOps = stringRedisTemplate.boundHashOps(userKey);
        return cartOps;
    }
}






















