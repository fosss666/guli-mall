package com.fosss.gulimall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fosss.common.utils.PageUtils;
import com.fosss.gulimall.order.entity.OrderEntity;
import com.fosss.gulimall.order.vo.OrderConfirmVo;

import java.util.Map;

/**
 * 订单
 *
 * @author fmh
 * @email 1745179058@qq.com
 * @date 2023-01-13 16:35:15
 */
public interface OrderService extends IService<OrderEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 封装订单确认模型
     */
    OrderConfirmVo getOrderConfirmData();
}

