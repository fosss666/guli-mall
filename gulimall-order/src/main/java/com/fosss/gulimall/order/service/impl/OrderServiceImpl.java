package com.fosss.gulimall.order.service.impl;

import com.fosss.common.vo.MemberRespVo;
import com.fosss.gulimall.order.feign.MemberFeignService;
import com.fosss.gulimall.order.interceptor.OrderInterceptor;
import com.fosss.gulimall.order.vo.MemberAddressVo;
import com.fosss.gulimall.order.vo.OrderConfirmVo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fosss.common.utils.PageUtils;
import com.fosss.common.utils.Query;

import com.fosss.gulimall.order.dao.OrderDao;
import com.fosss.gulimall.order.entity.OrderEntity;
import com.fosss.gulimall.order.service.OrderService;

import javax.annotation.Resource;


@Service("orderService")
public class OrderServiceImpl extends ServiceImpl<OrderDao, OrderEntity> implements OrderService {

    @Resource
    private MemberFeignService memberFeignService;

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
    public OrderConfirmVo getOrderConfirmData() {
        OrderConfirmVo orderConfirmVo = new OrderConfirmVo();

        //远程获取收货地址
        //获取当前用户
        MemberRespVo memberRespVo = OrderInterceptor.threadLocal.get();
        List<MemberAddressVo> memberReceiveAddressList = memberFeignService.getMemberReceiveAddressList(memberRespVo.getId());
        orderConfirmVo.setMemberAddressVos(memberReceiveAddressList);



        return null;
    }

}





















