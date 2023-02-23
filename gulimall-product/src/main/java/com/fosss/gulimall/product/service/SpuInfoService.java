package com.fosss.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fosss.common.utils.PageUtils;
import com.fosss.gulimall.product.entity.SpuInfoEntity;
import com.fosss.gulimall.product.vo.SpuSaveVo;

import java.util.Map;

/**
 * spu信息
 *
 * @author fmh
 * @email 1745179058@qq.com
 * @date 2023-01-12 17:50:29
 */
public interface SpuInfoService extends IService<SpuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
    /**
     * 保存spu完整信息
     */
    void saveSpu(SpuSaveVo spuSaveVo);
    /**
     * spu检索
     */
    PageUtils listByConditions(Map<String, Object> params);
    /**
     * 商品上架
     * /product/spuinfo/{spuId}/up
     */
    void up(Long spuId);
}

