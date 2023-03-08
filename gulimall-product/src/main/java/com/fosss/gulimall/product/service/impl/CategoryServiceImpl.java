package com.fosss.gulimall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fosss.common.constant.RedisConstant;
import com.fosss.gulimall.product.service.CategoryBrandRelationService;
import com.fosss.gulimall.product.vo.Catelog2Vo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fosss.common.utils.PageUtils;
import com.fosss.common.utils.Query;

import com.fosss.gulimall.product.dao.CategoryDao;
import com.fosss.gulimall.product.entity.CategoryEntity;
import com.fosss.gulimall.product.service.CategoryService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

import static com.fosss.common.constant.RedisConstant.CACHE_OTHER_TIME;

@Slf4j
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {
    @Resource
    private CategoryBrandRelationService categoryBrandRelationService;
    @Resource
    private StringRedisTemplate stringRelationService;

    /**
     * 逻辑删除
     */
    @Override
    @Transactional
    public void removeMenu(List<Long> catIds) {
        //删除品牌分类关系表中的数据
        categoryBrandRelationService.deleteCategories(catIds);

        //实施删除
        baseMapper.deleteBatchIds(catIds);
    }

    /**
     * 修改,同时修改品牌分类关系表中的数据
     */
    @Override
    @Transactional
    public void updateDetails(CategoryEntity category) {
        baseMapper.updateById(category);
        categoryBrandRelationService.updateDetails(category.getCatId(), category.getName());
    }

    /**
     * 查询所有的分类，并组装成树形(父子)结构返回
     */
    @Override
    public List<CategoryEntity> listTree() {
        //查询所有分类
        List<CategoryEntity> categoryList = baseMapper.selectList(null);
        //封装树形结构
        List<CategoryEntity> tree = new ArrayList<>();
        //组装树形结构
        for (CategoryEntity category : categoryList) {
            //获取一级分类
            if (category.getParentCid().equals(0L)) {
                //为一级分类设置子分类及为子分类设置子分类--递归
                setChildrenCategory(category, categoryList);
                tree.add(category);
            }
        }
        return tree;
    }

    /**
     * 递归设置子分类
     *
     * @param parentCategory 父分类
     * @param categoryList   总分类
     */
    private void setChildrenCategory(CategoryEntity parentCategory, List<CategoryEntity> categoryList) {
        parentCategory.setChildren(new ArrayList<>());
        for (CategoryEntity category : categoryList) {
            if (category.getParentCid().equals(parentCategory.getCatId())) {
                parentCategory.getChildren().add(category);
                setChildrenCategory(category, categoryList);

                //将子分类按照sort进行升序排序
                if (parentCategory.getChildren() != null && parentCategory.getChildren().size() > 0) {
                    parentCategory.getChildren().sort(new Comparator<CategoryEntity>() {
                        @Override
                        public int compare(CategoryEntity o1, CategoryEntity o2) {
                            //升序排序
                            return o1.getSort() - o2.getSort();

                        }
                    });
                }
            }
        }

    }

    /**
     * 查询一级分类
     * 缓存自定义 1.key  2.缓存时间 （配置文件中指定）  3.缓存值格式（json,默认是序列化后的值）
     */
    @Cacheable(cacheNames = {"category"}, key = "#root.methodName")//将返回数据缓存起来
    @Override
    public List<CategoryEntity> getLevel1() {
        log.info("从数据库中查询了一级分类");
        return baseMapper.selectList(new LambdaQueryWrapper<CategoryEntity>().eq(CategoryEntity::getCatLevel, 1));
    }

    /**
     * 获取二三级分类
     * 使用缓存进行优化
     * 解决缓存可能出现的问题：
     * 1.如果数据库中不存在，则短时间缓存null：解决缓存穿透问题（大量访问不存在的值，缓存中没有，同时到数据库中查询）
     * 2.为缓存设置不同的过期时间：解决缓存雪崩问题（缓存同时过期）
     * 3.加锁：解决缓存击穿问题（热点数据在缓存过期后被大量访问）
     */
    @Override
    public Map<String, List<Catelog2Vo>> getCatelogJson() {
        ValueOperations<String, String> ops = stringRelationService.opsForValue();
        String categoryJson = ops.get(RedisConstant.PRODUCT_CATEGORY_KEY);
        if (StringUtils.isEmpty(categoryJson)) {
            //缓存中没有数据，则从数据库中查询
            Map<String, List<Catelog2Vo>> listMap = getCatelog();
            //存入缓存
            //解决缓存1问题
            if (listMap == null || listMap.size() == 0) {
                //缓存空数据
                ops.set(RedisConstant.PRODUCT_CATEGORY_KEY, "null", RedisConstant.CACHE_NULL_TIME, TimeUnit.SECONDS);
            } else {
                String s = JSON.toJSONString(listMap);
                ops.set(RedisConstant.PRODUCT_CATEGORY_KEY, s, CACHE_OTHER_TIME, TimeUnit.SECONDS);
            }
            //返回数据
            return listMap;
        }
        //缓存中有数据，则取出并转为所需类型并返回
        if (categoryJson.equals("null")) {
            return Collections.emptyMap();
        }
        Map<String, List<Catelog2Vo>> stringListMap = JSON.parseObject(categoryJson, new TypeReference<Map<String, List<Catelog2Vo>>>() {
        });
        return stringListMap;
    }

    /**
     * 分布式锁解决缓存击穿，分布式项目应采用
     */
    private Map<String, List<Catelog2Vo>> getCatelog() {
        //性能优化-空间换时间-遍多次查询数据库为一次查询数据库
        //查询所有数据
        List<CategoryEntity> selectList = baseMapper.selectList(null);

        //获取一级分类
        //List<CategoryEntity> level1 = getLevel1();
        List<CategoryEntity> level1 = getChildren(selectList, 0L);
        //对每一个一级分类封装对应的二级分类
        Map<String, List<Catelog2Vo>> res = level1.stream().collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {
            //查询当前id对应的二级分类
            //List<CategoryEntity> level2 = baseMapper.selectList(new LambdaQueryWrapper<CategoryEntity>().eq(CategoryEntity::getParentCid, v.getCatId()));
            List<CategoryEntity> level2 = getChildren(selectList, v.getCatId());
            List<Catelog2Vo> collect = level2.stream().map(l2 -> {
                Catelog2Vo catelog2Vo = new Catelog2Vo();
                catelog2Vo.setId(l2.getCatId().toString());
                catelog2Vo.setCatalog1Id(v.getCatId().toString());
                catelog2Vo.setName(l2.getName());
                //查询当前二级分类对应的三级分类
                //List<CategoryEntity> level3 = baseMapper.selectList(new LambdaQueryWrapper<CategoryEntity>().eq(CategoryEntity::getParentCid, l2.getCatId()));
                List<CategoryEntity> level3 = getChildren(selectList, l2.getCatId());
                //将三级分类封装成所需vo
                List<Catelog2Vo.Category3Vo> l3List = level3.stream().map(l3 -> {
                    Catelog2Vo.Category3Vo category3Vo = new Catelog2Vo.Category3Vo();
                    category3Vo.setId(l3.getCatId().toString());
                    category3Vo.setName(l3.getName());
                    category3Vo.setCatalog2Id(l2.getCatId().toString());
                    return category3Vo;
                }).collect(Collectors.toList());
                catelog2Vo.setCatalog3List(l3List);
                return catelog2Vo;
            }).collect(Collectors.toList());
            return collect;
        }));

        return res;

    }

    /**
     * 加本地锁解决缓存击穿，单体项目可采用
     */
    //private Map<String, List<Catelog2Vo>> getCatelog() {
    //    //加本地锁，单体项目可用
    //    synchronized (this) {
    //        //性能优化-空间换时间-遍多次查询数据库为一次查询数据库
    //        //查询所有数据
    //        List<CategoryEntity> selectList = baseMapper.selectList(null);
    //
    //        //获取一级分类
    //        //List<CategoryEntity> level1 = getLevel1();
    //        List<CategoryEntity> level1 = getChildren(selectList, 0L);
    //        //对每一个一级分类封装对应的二级分类
    //        Map<String, List<Catelog2Vo>> res = level1.stream().collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {
    //            //查询当前id对应的二级分类
    //            //List<CategoryEntity> level2 = baseMapper.selectList(new LambdaQueryWrapper<CategoryEntity>().eq(CategoryEntity::getParentCid, v.getCatId()));
    //            List<CategoryEntity> level2 = getChildren(selectList, v.getCatId());
    //            List<Catelog2Vo> collect = level2.stream().map(l2 -> {
    //                Catelog2Vo catelog2Vo = new Catelog2Vo();
    //                catelog2Vo.setId(l2.getCatId().toString());
    //                catelog2Vo.setCatalog1Id(v.getCatId().toString());
    //                catelog2Vo.setName(l2.getName());
    //                //查询当前二级分类对应的三级分类
    //                //List<CategoryEntity> level3 = baseMapper.selectList(new LambdaQueryWrapper<CategoryEntity>().eq(CategoryEntity::getParentCid, l2.getCatId()));
    //                List<CategoryEntity> level3 = getChildren(selectList, l2.getCatId());
    //                //将三级分类封装成所需vo
    //                List<Catelog2Vo.Category3Vo> l3List = level3.stream().map(l3 -> {
    //                    Catelog2Vo.Category3Vo category3Vo = new Catelog2Vo.Category3Vo();
    //                    category3Vo.setId(l3.getCatId().toString());
    //                    category3Vo.setName(l3.getName());
    //                    category3Vo.setCatalog2Id(l2.getCatId().toString());
    //                    return category3Vo;
    //                }).collect(Collectors.toList());
    //                catelog2Vo.setCatalog3List(l3List);
    //                return catelog2Vo;
    //            }).collect(Collectors.toList());
    //            return collect;
    //        }));
    //
    //        //将加入缓存操作放在释放锁之前!!
    //        ValueOperations<String, String> ops = stringRelationService.opsForValue();
    //        if (res.size() == 0) {
    //            //缓存空数据
    //            ops.set(RedisConstant.PRODUCT_CATEGORY_KEY, "null", RedisConstant.CACHE_NULL_TIME, TimeUnit.SECONDS);
    //        } else {
    //            String s = JSON.toJSONString(res);
    //            ops.set(RedisConstant.PRODUCT_CATEGORY_KEY, s, CACHE_OTHER_TIME, TimeUnit.SECONDS);
    //        }
    //
    //        return res;
    //    }
    //}

    //获取子分类
    private List<CategoryEntity> getChildren(List<CategoryEntity> selectList, Long parentCid) {
        return selectList.stream().filter(item -> item.getParentCid().equals(parentCid)).collect(Collectors.toList());
    }
}

















