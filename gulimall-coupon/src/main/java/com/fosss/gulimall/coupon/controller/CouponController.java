package com.fosss.gulimall.coupon.controller;

import java.util.Arrays;
import java.util.Map;

import com.fosss.gulimall.coupon.feign.BrandFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fosss.gulimall.coupon.entity.CouponEntity;
import com.fosss.gulimall.coupon.service.CouponService;
import com.fosss.common.utils.PageUtils;
import com.fosss.common.utils.R;

import javax.annotation.Resource;


/**
 * 优惠券信息
 *
 * @author fmh
 * @email 1745179058@qq.com
 * @date 2023-01-13 16:21:28
 */
@RefreshScope//刷新并获取配置
@RestController
@RequestMapping("coupon/coupon")
public class CouponController {
    @Autowired
    private CouponService couponService;
    @Resource
    private BrandFeignService brandFeignService;

    //测试配置中心
    @Value("${coupon.user.name}")
    private String name;
    @Value("${coupon.user.age}")
    private Integer age;

    @RequestMapping("/test/config")
    public R testConfig(){
        return R.ok().put("name",name).put("age",age);
    }

    /**
     * 测试远程调用的测试接口
     */
    @RequestMapping("/test/brands")
    public R testBrandsFeign() {
        R r = brandFeignService.testGetBrands();
        return R.ok().put("brands", r.get("brands"));
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = couponService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        CouponEntity coupon = couponService.getById(id);

        return R.ok().put("coupon", coupon);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody CouponEntity coupon) {
        couponService.save(coupon);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody CouponEntity coupon) {
        couponService.updateById(coupon);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        couponService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
