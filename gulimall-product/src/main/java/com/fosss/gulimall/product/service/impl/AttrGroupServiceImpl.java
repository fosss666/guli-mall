package com.fosss.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fosss.common.constant.ProductConstant;
import com.fosss.common.utils.PageUtils;
import com.fosss.gulimall.product.dao.AttrGroupDao;
import com.fosss.gulimall.product.dao.CategoryDao;
import com.fosss.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.fosss.gulimall.product.entity.AttrEntity;
import com.fosss.gulimall.product.entity.AttrGroupEntity;
import com.fosss.gulimall.product.entity.CategoryEntity;
import com.fosss.gulimall.product.service.AttrAttrgroupRelationService;
import com.fosss.gulimall.product.service.AttrGroupService;
import com.fosss.gulimall.product.service.AttrService;
import com.fosss.gulimall.product.vo.AttrGroupRelationVo;
import com.fosss.gulimall.product.vo.AttrGroupWithAttrsVo;
import com.fosss.gulimall.product.vo.SpuItemAttrGroupVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Slf4j
@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Resource
    private AttrAttrgroupRelationService relationService;
    @Resource
    private AttrService attrService;
    @Resource
    private AttrGroupDao attrGroupDao;


    //@Override
    //public PageUtils queryPage(Map<String, Object> params) {
    //    IPage<AttrGroupEntity> page = this.page(
    //            new Query<AttrGroupEntity>().getPage(params),
    //            new QueryWrapper<AttrGroupEntity>()
    //    );
    //
    //    return new PageUtils(page);
    //}

    /**
     * 获取分类属性分组
     *
     * @param catelogId 所属分类id
     */
    @Override
    public PageUtils queryPageList(Map<String, Object> params, Long catelogId) {
        Object page = params.get("page");
        Object limit = params.get("limit");
        String sixdx = "" + params.get("sidx");//排序字段
        String order = "" + params.get("order");//排序方式
        String key = "" + params.get("key");//检索关键词

        //log.debug("字段：：：：：：");
        //log.debug("" + page);
        //log.debug("" + limit);
        //log.debug("" + sixdx);
        //log.debug("" + order);
        //log.debug("" + key);

        LambdaQueryWrapper<AttrGroupEntity> wrapper = new LambdaQueryWrapper<>();
        Page<AttrGroupEntity> iPage = new Page<>(Integer.parseInt("" + page), Integer.parseInt("" + limit));

        // else 条件查询
        //log.info(key);
        //boolean flag = StringUtils.isEmpty(key);
        //boolean aNull = key.equals("null");
        //不知道为什么传来的是个"null"这个字符串
        if (!StringUtils.isEmpty(key) && !key.equals("null")) {
            wrapper
                    .like(AttrGroupEntity::getAttrGroupId, key)
                    .or().like(AttrGroupEntity::getAttrGroupName, key)
                    .or().like(AttrGroupEntity::getDescript, key);
        }

        //和前端约定如果catelogId为0，则查询全部
        if (catelogId == 0) {
            IPage<AttrGroupEntity> attrGroupEntityIPage = baseMapper.selectPage(iPage, wrapper);
            return new PageUtils(attrGroupEntityIPage);
        }
        //if (sixdx!=null&&sixdx.length()>0&&order!=null&&order.length()>0) {
        //    wrapper.eq(AttrGroupEntity::getCatelogId, catelogId)
        //            .last("ORDER BY " + sixdx + " " + order);
        //}
        wrapper.eq(AttrGroupEntity::getCatelogId, catelogId);
        baseMapper.selectPage(iPage, wrapper);

        return new PageUtils(iPage);

    }

    /**
     * 信息
     */
    @Resource
    private CategoryDao categoryDao;

    @Override
    public AttrGroupEntity getInfo(Long attrGroupId) {
        //获取该分组
        AttrGroupEntity attrGroup = baseMapper.selectById(attrGroupId);
        //获取该分类
        CategoryEntity categoryEntity = categoryDao.selectById(attrGroup.getCatelogId());
        //设置分组的完整路径，用于级联选择器的回显
        LinkedList<Long> list = new LinkedList<>();
        LinkedList<Long> catelogPath = getCatelogPath(categoryEntity, list);
        attrGroup.setCatelogPath(catelogPath.toArray(new Long[catelogPath.size()]));
        return attrGroup;
    }

    /**
     * 获取分类完整路径
     */
    public LinkedList<Long> getCatelogPath(CategoryEntity categoryEntity, LinkedList<Long> list) {
        //向前插入父id
        list.addFirst(categoryEntity.getCatId());

        //说明有父分类
        if (categoryEntity.getParentCid() != 0) {
            CategoryEntity parent = categoryDao.selectById(categoryEntity.getParentCid());
            getCatelogPath(parent, list);
        }
        return list;
    }

    /**
     * 获取关联属性
     */
    @Override
    public List<AttrEntity> getAttrRelation(Long attrgroupId) {
        LambdaQueryWrapper<AttrAttrgroupRelationEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AttrAttrgroupRelationEntity::getAttrGroupId, attrgroupId);
        List<AttrAttrgroupRelationEntity> relationEntities = relationService.list(wrapper);
        List<AttrEntity> list = new ArrayList<>();
        if (relationEntities != null && relationEntities.size() > 0) {
            relationEntities.forEach((relation) -> {
                Long attrId = relation.getAttrId();
                AttrEntity attrEntity = attrService.getById(attrId);
                if (attrEntity != null) {
                    list.add(attrEntity);
                }
            });
        }
        return list;
    }

    /**
     * 删除分组关联的属性
     *
     * @param attrGroupRelationVo
     */
    @Override
    public void deleteAttrRelation(AttrGroupRelationVo[] attrGroupRelationVo) {
        //转为关联类
        AttrAttrgroupRelationEntity[] relationEntities = new AttrAttrgroupRelationEntity[attrGroupRelationVo.length];
        for (int i = 0; i < attrGroupRelationVo.length; i++) {
            AttrAttrgroupRelationEntity relation = new AttrAttrgroupRelationEntity();
            relation.setAttrId(attrGroupRelationVo[i].getAttrId());
            relation.setAttrGroupId(attrGroupRelationVo[i].getAttrGroupId());
            relationEntities[i] = relation;
        }
        //进行批量删除
        relationService.removeRelations(relationEntities);
    }

    /**
     * 获取该分组gai未关联的属性，用于新增关联
     */
    @Override
    public PageUtils getNotRelation(Long attrgroupId, Map<String, Object> params) {
        //！！！！！！！！！！！！！只能关联当前分类当前分组的未被其他分组关联过的基本属性
        //获取分类id
        AttrGroupEntity attrGroupEntity = baseMapper.selectById(attrgroupId);
        Long catelogId = attrGroupEntity.getCatelogId();

        //获取当前分类的所有分组已经关联过的属性id
        List<AttrGroupEntity> attrGroupEntities = attrGroupDao.selectList(new LambdaQueryWrapper<AttrGroupEntity>().eq(AttrGroupEntity::getCatelogId, catelogId));
        List<Long> attrGroupIds = attrGroupEntities.stream().map(AttrGroupEntity::getAttrGroupId).collect(Collectors.toList());
        List<AttrAttrgroupRelationEntity> relationedAttrs = relationService.list(new LambdaQueryWrapper<AttrAttrgroupRelationEntity>().in(AttrAttrgroupRelationEntity::getAttrGroupId, attrGroupIds));
        List<Long> relationedAttrIds = relationedAttrs.stream().map(AttrAttrgroupRelationEntity::getAttrId).collect(Collectors.toList());

        //查询没有当前分类下被当前及其他分组关联过的基本属性
        String key = params.get("key").toString();
        long limit = Long.parseLong(params.get("limit").toString());
        long page = Long.parseLong(params.get("page").toString());

        IPage<AttrEntity> ipage = new Page<>(page, limit);

        LambdaQueryWrapper<AttrEntity> attrWrapper = new LambdaQueryWrapper<>();
        attrWrapper
                .eq(AttrEntity::getAttrType, ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode())
                .notIn(AttrEntity::getAttrId, relationedAttrIds)//attrEntityLambdaQueryWrapper.in(AttrEntity::getAttrId, relationedAttrIds);
                //or的位置一定要放在最后！！！！
                .like(!StringUtils.isEmpty(key), AttrEntity::getAttrId, key)
                .or().like(!StringUtils.isEmpty(key), AttrEntity::getAttrName, key)
                .or().like(!StringUtils.isEmpty(key), AttrEntity::getShowDesc, key);
        attrService.page(ipage, attrWrapper);


        return new PageUtils(ipage);
    }

    /**
     * 添加属性与分组关联关系
     *
     * @param attrGroupRelationVos
     */
    @Override
    public void addRelation(List<AttrGroupRelationVo> attrGroupRelationVos) {
        for (AttrGroupRelationVo attrGroupRelationVo : attrGroupRelationVos) {
            Long attrId = attrGroupRelationVo.getAttrId();
            Long attrGroupId = attrGroupRelationVo.getAttrGroupId();
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            relationEntity.setAttrId(attrId);
            relationEntity.setAttrGroupId(attrGroupId);
            relationService.save(relationEntity);
        }

    }

    /**
     * 获取分类下所有分组及其属性
     */
    @Override
    public List<AttrGroupWithAttrsVo> getGroupAndAttrByCatelogId(Long catelogId) {
        //1.查询此分类的所有分组
        List<AttrGroupEntity> attrGroupEntities = baseMapper.selectList(new LambdaQueryWrapper<AttrGroupEntity>().eq(AttrGroupEntity::getCatelogId, catelogId));
        List<AttrGroupWithAttrsVo> res = new ArrayList<>();
        //2.查询冰封转分组下所有属性
        for (AttrGroupEntity attrGroupEntity : attrGroupEntities) {
            AttrGroupWithAttrsVo attrGroupWithAttrsVo = new AttrGroupWithAttrsVo();
            BeanUtils.copyProperties(attrGroupEntity, attrGroupWithAttrsVo);
            //查询当前分组下的属性id
            List<AttrAttrgroupRelationEntity> relationEntities = relationService.list(new LambdaQueryWrapper<AttrAttrgroupRelationEntity>().eq(AttrAttrgroupRelationEntity::getAttrGroupId, attrGroupEntity.getAttrGroupId()));
            //获得当前分组下的属性
            List<Long> attrIdList = relationEntities.stream().map((item) -> {
                return item.getAttrId();
            }).collect(Collectors.toList());
            List<AttrEntity> attrList = new ArrayList<>(attrService.listByIds(attrIdList));
            attrGroupWithAttrsVo.setAttrs(attrList);
            //存入res
            res.add(attrGroupWithAttrsVo);
        }
        return res;
    }

    /**
     * 获取spu的规格参数信息
     */
    @Override
    public List<SpuItemAttrGroupVo> getSpuItemAttrGroup(Long spuId, Long catalogId) {
        return baseMapper.getSpuItemAttrGroup(spuId, catalogId);
    }

}















