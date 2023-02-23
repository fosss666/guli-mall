package com.fosss.gulimall.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fosss.common.utils.R;
import com.fosss.gulimall.ware.feign.ProductFeignService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fosss.common.utils.PageUtils;
import com.fosss.common.utils.Query;

import com.fosss.gulimall.ware.dao.WareSkuDao;
import com.fosss.gulimall.ware.entity.WareSkuEntity;
import com.fosss.gulimall.ware.service.WareSkuService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;


@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {

    @Resource
    private ProductFeignService productFeignService;

    /**
     * 查询商品库存
     */
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<WareSkuEntity> wrapper = new LambdaQueryWrapper<>();
        String wareId = (String) params.get("wareId");
        String skuId = (String) params.get("skuId");
        wrapper
                .eq(!StringUtils.isEmpty(wareId), WareSkuEntity::getWareId, wareId)
                .eq(!StringUtils.isEmpty(skuId), WareSkuEntity::getSkuId, skuId);

        IPage<WareSkuEntity> page = this.page(
                new Query<WareSkuEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

    /**
     * 添加库存
     *
     * @param skuId  skuId
     * @param wareId 仓库id
     * @param skuNum 当前库存
     */
    @Transactional
    @Override
    public void addStock(Long skuId, Long wareId, Integer skuNum) {
        //判断是否有这个库存记录，没有的话进行添加操作，否则进行修改操作
        LambdaQueryWrapper<WareSkuEntity> wareSkuWrapper = new LambdaQueryWrapper<>();
        wareSkuWrapper.eq(WareSkuEntity::getWareId, wareId).eq(WareSkuEntity::getSkuId, skuId);
        WareSkuEntity wareSkuEntity = baseMapper.selectOne(wareSkuWrapper);
        if (wareSkuEntity == null) {
            //没有这个库存记录，执行添加操作
            wareSkuEntity = new WareSkuEntity();
            wareSkuEntity.setSkuId(skuId);
            wareSkuEntity.setWareId(wareId);
            wareSkuEntity.setStockLocked(0);//默认不锁定库存
            wareSkuEntity.setStock(skuNum);
            //远程调用微服务gulimall-product查询skuName
            //远程调用接口可能由于各种原因，如果失败的话，由于获取skuName这个功能并不非常重要，所以直接捕捉异常就行了，不用回滚事务
            try {
                R r = productFeignService.info(skuId);
                if (r.getCode() == 0) {
                    Map<String, Object> skuInfo = (Map<String, Object>) r.get("skuInfo");
                    String skuName = (String) skuInfo.get("skuName");
                    if (!StringUtils.isEmpty(skuName)) {
                        wareSkuEntity.setSkuName(skuName);
                    }
                }
            } catch (Exception e) {
                log.error("添加库存-远程调用商品服务失败");
            }

            baseMapper.insert(wareSkuEntity);
        } else {
            //执行修改库存操作
            wareSkuEntity.setStock(wareSkuEntity.getStock() + skuNum);
            baseMapper.updateById(wareSkuEntity);
        }
    }

    /**
     * 根据skuId查询是否有库存
     */
    @Override
    public Map<Long, Boolean> hasStock(List<Long> skuIds) {
        Map<Long, Boolean> map = new HashMap<>();
        for (Long skuId : skuIds) {
            //查询该sku的库存量
            int count = baseMapper.hasStock(skuId);
            map.put(skuId, count > 0);
        }
        return map;
    }

}
















