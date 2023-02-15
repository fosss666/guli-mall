package com.fosss.gulimall.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.additional.query.impl.LambdaQueryChainWrapper;
import com.fosss.common.constant.WareConstant;
import com.fosss.gulimall.ware.controller.WareInfoController;
import com.fosss.gulimall.ware.entity.PurchaseDetailEntity;
import com.fosss.gulimall.ware.service.PurchaseDetailService;
import com.fosss.gulimall.ware.vo.MergeVo;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fosss.common.utils.PageUtils;
import com.fosss.common.utils.Query;

import com.fosss.gulimall.ware.dao.PurchaseDao;
import com.fosss.gulimall.ware.entity.PurchaseEntity;
import com.fosss.gulimall.ware.service.PurchaseService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {

    @Resource
    private PurchaseDetailService purchaseDetailService;

    /**
     * 查询未领取的采购单
     */
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<PurchaseEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PurchaseEntity::getStatus, WareConstant.PurchaseStatusEnum.CREATED.getCode()).or().eq(PurchaseEntity::getStatus, WareConstant.PurchaseStatusEnum.ASSIGNED.getCode());
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

    /**
     * 合并采购需求
     * purchaseId: 1, //整单id
     * items:[1,2,3,4] //合并项集合
     *
     * @param mergeVo
     */
    @Transactional
    @Override
    public void merge(MergeVo mergeVo) {
        //先判断有没有订购单id,如果没有，则自动创建
        Long purchaseId = mergeVo.getPurchaseId();
        if (purchaseId == null) {
            PurchaseEntity purchaseEntity = new PurchaseEntity();
            purchaseEntity.setCreateTime(new Date());
            purchaseEntity.setUpdateTime(new Date());
            purchaseEntity.setStatus(WareConstant.PurchaseStatusEnum.CREATED.getCode());
            baseMapper.insert(purchaseEntity);
            //设置订购单id
            purchaseId = purchaseEntity.getId();
        }

        // 确认采购单状态是0,1才可以合并
        //查询采购单状态
        PurchaseEntity purchase = baseMapper.selectById(purchaseId);
        if (purchase.getStatus() == WareConstant.PurchaseStatusEnum.CREATED.getCode() || purchase.getStatus() == WareConstant.PurchaseStatusEnum.ASSIGNED.getCode()) {
            //获取要合并的采购需求
            List<Long> items = mergeVo.getItems();
            Long finalPurchaseId = purchaseId;
            List<PurchaseDetailEntity> collect = items.stream().map(id -> {
                PurchaseDetailEntity purchaseDetailEntity = new PurchaseDetailEntity();
                purchaseDetailEntity.setId(id);
                purchaseDetailEntity.setStatus(WareConstant.PurchaseDetailStatusEnum.ASSIGNED.getCode());
                //Variable used in lambda expression should be final or effectively final
                //lambda表达式中使用的变量应该是final或者有效的final，也就是说，lambda 表达式只能引用标记了 final 的外层局部变量，这就是说不能在 lambda 内部修改定义在域外的局部变量，否则会编译错误
                //在lambda表达式中对变量的操作都是基于原变量的副本，不会影响到原变量的值。
                purchaseDetailEntity.setPurchaseId(finalPurchaseId);
                return purchaseDetailEntity;
            }).collect(Collectors.toList());
            //修改采购详情（合并需求）
            purchaseDetailService.updateBatchById(collect);
            //更新采购时间
            PurchaseEntity purchaseEntity = baseMapper.selectById(purchaseId);
            purchaseEntity.setUpdateTime(new Date());
            baseMapper.updateById(purchaseEntity);
        }

    }

    /**
     * 查询采购单
     */
    @Override
    public PageUtils list(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<>()
        );
        return new PageUtils(page);
    }

    /**
     * 领取采购单
     *
     * @param purchaseIds 采购单id
     */
    @Transactional
    @Override
    public void received(List<Long> purchaseIds) {
        //1.过滤掉领取过的采购单
        List<PurchaseEntity> collect = purchaseIds.stream().map((id) -> {
            return baseMapper.selectById(id);
        }).filter((item) -> {
            return item.getStatus() == WareConstant.PurchaseStatusEnum.CREATED.getCode() || item.getStatus() == WareConstant.PurchaseStatusEnum.ASSIGNED.getCode();
        }).map((item) -> {
            item.setStatus(WareConstant.PurchaseStatusEnum.RECEIVE.getCode());
            item.setUpdateTime(new Date());
            return item;
        }).collect(Collectors.toList());

        //2.更改采购单状态
        for (PurchaseEntity purchaseEntity : collect) {
            baseMapper.updateById(purchaseEntity);
        }

        //3.更改采购需求状态
        for (PurchaseEntity purchase : collect) {
            LambdaQueryWrapper<PurchaseDetailEntity> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(PurchaseDetailEntity::getPurchaseId, purchase.getId());
            List<PurchaseDetailEntity> purchaseDetailEntities = purchaseDetailService.list(wrapper);
            for (PurchaseDetailEntity purchaseDetailEntity : purchaseDetailEntities) {
                purchaseDetailEntity.setStatus(WareConstant.PurchaseDetailStatusEnum.BUYING.getCode());
            }
            purchaseDetailService.updateBatchById(purchaseDetailEntities);
        }

    }
}


























