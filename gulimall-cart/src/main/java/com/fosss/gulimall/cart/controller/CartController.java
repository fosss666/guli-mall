package com.fosss.gulimall.cart.controller;

import com.fosss.gulimall.cart.interceptor.GulimallInterceptor;
import com.fosss.gulimall.cart.service.CartService;
import com.fosss.gulimall.cart.vo.CartItemVo;
import com.fosss.gulimall.cart.vo.CartVo;
import com.fosss.gulimall.cart.vo.UserInfoTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;

/**
 * @author: fosss
 * Date: 2023/5/21
 * Time: 13:44
 * Description:
 */
@Controller
public class CartController {

    @Resource
    private CartService cartService;

    /**
     * 删除购物项
     */
    @GetMapping("/deleteItem")
    public String deleteItem(@RequestParam("skuId") Long skuId) {
        cartService.deleteItem(skuId);
        return "redirect:http://cart.gulimall.com/cart.html";
    }

    /**
     * 修改数量
     */
    @GetMapping("/countItem")
    public String countItem(@RequestParam("skuId") Long skuId,
                            @RequestParam("num") Integer num) {
        cartService.countItem(skuId, num);
        return "redirect:http://cart.gulimall.com/cart.html";
    }

    /**
     * 选中购物项
     */
    @GetMapping("/checkItem")
    public String checkItem(@RequestParam("skuId") Long skuId,
                            @RequestParam("check") Integer check) {
        cartService.checkItem(skuId, check);
        return "redirect:http://cart.gulimall.com/cart.html";
    }

    /**
     * 将商品添加到购物车
     * 如果直接跳转到success页面的话，会存在问题，当网页刷新时会重复执行添加购物车方法，导致商品数量增加
     * 解决方案:重定向到另一个方法，携带参数skuId,在那个方法中根据skuId从redis中查询购物车数据，这样网页再刷新时，一直刷新的是重定向后的url，就不会出现
     * 重复添加的问题了
     */
    @GetMapping("/addCartItem")
    public String addToCart(@RequestParam("skuId") Long skuId,
                            @RequestParam("num") Integer num,
                            RedirectAttributes redirectAttributes) throws ExecutionException, InterruptedException {
        CartItemVo cartItemVo = cartService.addToCart(skuId, num);
        //model.addAttribute("item", cartItemVo);
        //会将skuId拼接到url后面
        redirectAttributes.addAttribute("skuId", skuId);
        return "redirect:http://cart.gulimall.com/addToCartSuccess.html/"+skuId;
    }

    /**
     * 从redis中查询购物车
     */
    @GetMapping("addToCartSuccess.html/{skuId}")
    public String addToCartSuccess(@PathVariable("skuId") Long skuId,
                                   Model model) {
        CartItemVo cartItemVo = cartService.searchCartItem(skuId);
        model.addAttribute("item", cartItemVo);
        return "success";
    }

    /**
     * 获取购物车
     * 浏览器有一个cookie:user-key 标识用户的身份，一个月过期
     * 如果第一次使用jd的购物车功能，都会给一个临时的用户身份:
     * 浏览器以后保存，每次访问都会带上这个cookie；
     * <p>
     * 登录：session有
     * 没登录：按照cookie里面带来user-key来做
     * 第一次，如果没有临时用户，自动创建一个临时用户
     */
    @RequestMapping("/cart.html")
    public String cartList(Model model) throws ExecutionException, InterruptedException {
        //从threadLocal中获取用户信息
        //UserInfoTo userInfoTo = GulimallInterceptor.threadLocal.get();
        //System.out.println(userInfoTo);
        CartVo cartVo = cartService.getCart();
        model.addAttribute("cart", cartVo);
        return "cartList";
    }
}




















