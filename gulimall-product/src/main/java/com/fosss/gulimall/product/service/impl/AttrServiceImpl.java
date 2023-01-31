package com.fosss.gulimall.product.service.impl;

import com.fosss.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.fosss.gulimall.product.service.AttrAttrgroupRelationService;
import com.fosss.gulimall.product.vo.AttrVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fosss.common.utils.PageUtils;
import com.fosss.common.utils.Query;

import com.fosss.gulimall.product.dao.AttrDao;
import com.fosss.gulimall.product.entity.AttrEntity;
import com.fosss.gulimall.product.service.AttrService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Slf4j
@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {

    @Resource
    private AttrAttrgroupRelationService relationService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 保存属性
     */
    @Override
    @Transactional
    public void saveAttr(AttrVo attr) {
        //1.保存到属性表
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attr, attrEntity);
        baseMapper.insert(attrEntity);
        //2.保存到属性和属性分组关系表
        AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();
        attrAttrgroupRelationEntity.setAttrId(attrEntity.getAttrId());
        attrAttrgroupRelationEntity.setAttrGroupId(attr.getAttrGroupId());
        relationService.save(attrAttrgroupRelationEntity);
    }

}













