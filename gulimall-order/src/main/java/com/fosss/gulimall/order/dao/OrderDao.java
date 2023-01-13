package com.fosss.gulimall.order.dao;

import com.fosss.gulimall.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author fmh
 * @email 1745179058@qq.com
 * @date 2023-01-13 16:35:15
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
