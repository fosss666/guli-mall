package com.fosss.gulimall.product.vo;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @author: fosss
 * Date: 2023/4/3
 * Time: 20:58
 * Description: 商品详情销售属性
 */
@Data
@ToString
public class SkuItemSaleAttrVo {
    private Long attrId;

    private String attrName;

    private String attrValues;
}
