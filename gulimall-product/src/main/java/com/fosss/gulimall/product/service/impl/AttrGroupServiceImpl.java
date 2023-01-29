package com.fosss.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fosss.gulimall.product.dao.CategoryDao;
import com.fosss.gulimall.product.entity.CategoryEntity;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fosss.common.utils.PageUtils;

import com.fosss.gulimall.product.dao.AttrGroupDao;
import com.fosss.gulimall.product.entity.AttrGroupEntity;
import com.fosss.gulimall.product.service.AttrGroupService;

import javax.annotation.Resource;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

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
        if (StringUtils.isNotEmpty(key)) {
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

        wrapper.eq(AttrGroupEntity::getCatelogId, catelogId)
                .last(StringUtils.isNotEmpty(sixdx) && StringUtils.isNotEmpty(order),
                        "ORDER BY " + sixdx + " " + order);
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
        getCatelogPath(categoryEntity, list);
        attrGroup.setCatelogPath(list.toArray(new Long[list.size()]));
        return attrGroup;
    }

    private void getCatelogPath(CategoryEntity categoryEntity, LinkedList<Long> list) {
        //向前插入父id
        list.addFirst(categoryEntity.getCatId());

        //说明有父分类
        if (categoryEntity.getParentCid() != 0) {
            CategoryEntity parent = categoryDao.selectById(categoryEntity.getParentCid());
            getCatelogPath(parent, list);
        }
    }

}







