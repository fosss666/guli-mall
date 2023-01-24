package com.fosss.gulimall.product;

import com.fosss.gulimall.product.entity.BrandEntity;
import com.fosss.gulimall.product.service.BrandService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GulimallProductApplicationTests {
    @Resource
    private BrandService brandService;


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
        while (iterator.hasNext()){
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















