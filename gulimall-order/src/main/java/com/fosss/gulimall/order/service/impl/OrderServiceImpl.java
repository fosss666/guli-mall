package com.fosss.gulimall.order.service.impl;

import com.fosss.common.vo.MemberRespVo;
import com.fosss.gulimall.order.feign.CartFeignService;
import com.fosss.gulimall.order.feign.MemberFeignService;
import com.fosss.gulimall.order.interceptor.OrderInterceptor;
import com.fosss.gulimall.order.vo.MemberAddressVo;
import com.fosss.gulimall.order.vo.OrderConfirmVo;
import com.fosss.gulimall.order.vo.OrderItemVo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fosss.common.utils.PageUtils;
import com.fosss.common.utils.Query;

import com.fosss.gulimall.order.dao.OrderDao;
import com.fosss.gulimall.order.entity.OrderEntity;
import com.fosss.gulimall.order.service.OrderService;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.annotation.Resource;


@Service("orderService")
public class OrderServiceImpl extends ServiceImpl<OrderDao, OrderEntity> implements OrderService {

    @Resource
    private MemberFeignService memberFeignService;
    @Resource
    private CartFeignService cartFeignService;
    @Resource
    private ThreadPoolExecutor executor;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OrderEntity> page = this.page(
                new Query<OrderEntity>().getPage(params),
                new QueryWrapper<OrderEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 封装订单确认模型
     */
    @Override
    public OrderConfirmVo getOrderConfirmData() throws ExecutionException, InterruptedException {
        OrderConfirmVo orderConfirmVo = new OrderConfirmVo();

        //远程获取收货地址
        //获取当前用户
        MemberRespVo memberRespVo = OrderInterceptor.threadLocal.get();
        //获取当前线程的请求头，然后放到其他线程中
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

        CompletableFuture<Void> memberAddressFuture = CompletableFuture.runAsync(() -> {
            RequestContextHolder.setRequestAttributes(requestAttributes);
            List<MemberAddressVo> memberReceiveAddressList = memberFeignService.getMemberReceiveAddressList(memberRespVo.getId());
            orderConfirmVo.setMemberAddressVos(memberReceiveAddressList);
        }, executor);

        //远程获取购物车中被选中的数据
        CompletableFuture<Void> cartFuture = CompletableFuture.runAsync(() -> {
            RequestContextHolder.setRequestAttributes(requestAttributes);
            List<OrderItemVo> checkedCart = cartFeignService.getCheckedCart();
            orderConfirmVo.setItems(checkedCart);
        }, executor);

        //获取用户积分
        Integer integration = memberRespVo.getIntegration();
        orderConfirmVo.setIntegration(integration);

        //线程都执行完后再返回
        CompletableFuture.allOf(memberAddressFuture, cartFuture).get();
        return orderConfirmVo;
    }

}





















