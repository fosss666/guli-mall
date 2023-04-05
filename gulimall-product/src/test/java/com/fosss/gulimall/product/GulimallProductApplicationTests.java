package com.fosss.gulimall.product;

import com.fosss.gulimall.product.dao.AttrGroupDao;
import com.fosss.gulimall.product.entity.AttrGroupEntity;
import com.fosss.gulimall.product.entity.BrandEntity;
import com.fosss.gulimall.product.service.AttrGroupService;
import com.fosss.gulimall.product.service.BrandService;
import com.fosss.gulimall.product.vo.SpuItemAttrGroupVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GulimallProductApplicationTests {
    @Resource
    private BrandService brandService;
    @Resource
    private AttrGroupService attrGroupService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private RedissonClient redissonClient;
    @Resource
    private AttrGroupDao attrGroupDao;

    @Test
    public void testGetSpuItemAttrGroup() {
        List<SpuItemAttrGroupVo> spuItemAttrGroup = attrGroupDao.getSpuItemAttrGroup(13L, 225L);
        System.out.println(spuItemAttrGroup);
    }

    /**
     * 测试redisson
     */
    @Test
    public void testRedisson() {
        System.out.println(redissonClient);
    }

    /**
     * 测试redis
     */
    @Test
    public void testRedis() {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        ops.set("hello", "world_" + UUID.randomUUID());
        String hello = ops.get("hello");
        System.out.println("hello = " + hello);
    }

    /**
     * 测试获取分类的完整路径
     */
    @Test
    public void testGetPath() {
        AttrGroupEntity info = attrGroupService.getInfo(1L);
        Long[] catelogPath = info.getCatelogPath();
        System.out.println("catelogPath = " + Arrays.toString(catelogPath));
    }

    /**
     * 测试添加品牌
     */
    @Test
    public void testInsert() {
        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setName("中兴");
        brandService.save(brandEntity);
    }


    /**
     * 测试查询品牌
     */
    @Test
    public void testSelect() {
        List<BrandEntity> list = brandService.list();
        Iterator<BrandEntity> iterator = list.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }

    /**
     * 测试修改品牌
     */
    @Test
    public void testUpdate() {
        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setBrandId(1l);
        brandEntity.setDescript("中华有为");
        brandService.updateById(brandEntity);

    }
}















