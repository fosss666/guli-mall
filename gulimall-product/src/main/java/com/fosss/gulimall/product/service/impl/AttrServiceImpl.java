package com.fosss.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.fosss.common.constant.ProductConstant;
import com.fosss.gulimall.product.dao.AttrAttrgroupRelationDao;
import com.fosss.gulimall.product.dao.AttrGroupDao;
import com.fosss.gulimall.product.dao.CategoryDao;
import com.fosss.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.fosss.gulimall.product.entity.AttrGroupEntity;
import com.fosss.gulimall.product.entity.CategoryEntity;
import com.fosss.gulimall.product.service.AttrAttrgroupRelationService;
import com.fosss.gulimall.product.service.AttrGroupService;
import com.fosss.gulimall.product.vo.AttrRespVo;
import com.fosss.gulimall.product.vo.AttrVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fosss.common.utils.PageUtils;
import com.fosss.common.utils.Query;

import com.fosss.gulimall.product.dao.AttrDao;
import com.fosss.gulimall.product.entity.AttrEntity;
import com.fosss.gulimall.product.service.AttrService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

@Slf4j
@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {

    @Resource
    private AttrAttrgroupRelationService relationService;
    @Resource
    private CategoryDao categoryDao;
    @Resource
    private AttrAttrgroupRelationDao attrAttrgroupRelationDao;
    @Resource
    private AttrGroupDao attrGroupDao;
    @Resource
    private AttrGroupService attrGroupService;

    @Override
    public PageUtils queryPage(Long catelogId, Map<String, Object> params, String type) {

        String key = params.get("key").toString();
        LambdaQueryWrapper<AttrEntity> wrapper = new LambdaQueryWrapper<AttrEntity>()
                .eq(catelogId != 0, AttrEntity::getCatelogId, catelogId)
                //需要判断是基本属性还是销售属性
                .eq(AttrEntity::getAttrType, "base".equals(type) ? ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode() : ProductConstant.AttrEnum.ATTR_TYPE_SALE.getCode())
                .like(!StringUtils.isEmpty(key), AttrEntity::getAttrName, key)
                .or().like(!StringUtils.isEmpty(key), AttrEntity::getShowDesc, key)
                .or().like(!StringUtils.isEmpty(key), AttrEntity::getAttrId, key);

        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                wrapper
        );
        PageUtils pageUtils = new PageUtils(page);
        //还需要设置分类名称和分组名称
        List<AttrEntity> records = page.getRecords();
        List<AttrRespVo> attrRespVoList = records.stream().map((attrEntity -> {
            AttrRespVo attrRespVo = new AttrRespVo();
            BeanUtils.copyProperties(attrEntity, attrRespVo);
            //查询并设置分类名称
            CategoryEntity categoryEntity = categoryDao.selectById(attrEntity.getCatelogId());
            if (categoryEntity != null) {
                attrRespVo.setCatelogName(categoryEntity.getName());
            }
            //查询并设置分组名称(基本属性才需要设置分组)
            //从分组和属性的关系表中查到分组id
            if (attrEntity.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()) {
                AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = attrAttrgroupRelationDao.selectOne(new LambdaQueryWrapper<AttrAttrgroupRelationEntity>().eq(AttrAttrgroupRelationEntity::getAttrId, attrEntity.getAttrId()));
                if (attrAttrgroupRelationEntity != null) {
                    //根据分组id查询分组名称
                    AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrAttrgroupRelationEntity.getAttrGroupId());
                    if (attrGroupEntity != null) {
                        attrRespVo.setGroupName(attrGroupEntity.getAttrGroupName());
                    }
                }
            }
            return attrRespVo;
        })).collect(Collectors.toList());
        pageUtils.setList(attrRespVoList);
        return pageUtils;
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
        //2.保存到属性和属性分组关系表(基本属性才需要设置分组)
        if (attr.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()) {
            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();
            attrAttrgroupRelationEntity.setAttrId(attrEntity.getAttrId());
            attrAttrgroupRelationEntity.setAttrGroupId(attr.getAttrGroupId());
            relationService.save(attrAttrgroupRelationEntity);
        }
    }

    /**
     * 查询属性详情
     */
    @Override
    @Transactional
    public AttrRespVo getInfo(Long attrId) {
        //先查询标准数据
        AttrEntity attrEntity = baseMapper.selectById(attrId);
        //设置返回数据
        AttrRespVo attrRespVo = new AttrRespVo();
        BeanUtils.copyProperties(attrEntity, attrRespVo);
        //查询所属分组(基本属性才需要设置分组)
        if (attrEntity.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()) {
            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = attrAttrgroupRelationDao.selectOne(new LambdaQueryWrapper<AttrAttrgroupRelationEntity>().eq(AttrAttrgroupRelationEntity::getAttrId, attrEntity.getAttrId()));
            if (attrAttrgroupRelationEntity != null) {
                attrRespVo.setAttrGroupId(attrAttrgroupRelationEntity.getAttrGroupId());
            }
        }
        //查询所属分类
        Long catelogId = attrEntity.getCatelogId();
        if (catelogId != null) {
            CategoryEntity categoryEntity = categoryDao.selectById(catelogId);
            LinkedList<Long> list = new LinkedList<>();
            //查询分类完整路径
            LinkedList<Long> catelogPath = attrGroupService.getCatelogPath(categoryEntity, list);
            attrRespVo.setCatelogPath(catelogPath.toArray(new Long[]{}));
        }

        return attrRespVo;
    }

    /**
     * 修改属性详情
     */
    @Override
    @Transactional
    public void updateAttr(AttrVo attr) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attr, attrEntity);
        //修改属性表
        baseMapper.updateById(attrEntity);
        //修改属性分组关系表(基本属性才需要设置分组)
        if (attr.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()) {
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            BeanUtils.copyProperties(attr, relationEntity);
            LambdaUpdateWrapper<AttrAttrgroupRelationEntity> wrapper = new LambdaUpdateWrapper<AttrAttrgroupRelationEntity>().eq(AttrAttrgroupRelationEntity::getAttrId, relationEntity.getAttrId());
            //如果原来没有分组的话，则应该执行的操作是添加操作，否则应执行修改操作，这里需要进行分别讨论
            //查询是否有分组
            Integer count = attrAttrgroupRelationDao.selectCount(wrapper);
            if (count == 0) {
                //执行添加
                attrAttrgroupRelationDao.insert(relationEntity);
            } else {
                attrAttrgroupRelationDao.update(
                        relationEntity,
                        wrapper
                );
            }
        }
    }
}













