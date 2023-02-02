package com.fosss.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Resource
    private AttrAttrgroupRelationService relationService;
    @Resource
    private AttrService attrService;

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
        relationEntities.forEach((relation) -> {
            Long attrId = relation.getAttrId();
            AttrEntity attrEntity = attrService.getById(attrId);
            list.add(attrEntity);
        });
        return list;
    }

}







