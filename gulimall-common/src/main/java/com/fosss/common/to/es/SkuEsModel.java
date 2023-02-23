package com.fosss.common.to.es;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author fosss
 * @date 2023/2/23
 * @description： 商品上架时沟通es服务和商品服务的统一类
 */
@Data
public class SkuEsModel {
    private Long skuId;
    private Long spuId;
    private String skuTitle;
    private BigDecimal skuPrice;
    private String skuImg;
    private Long saleCount;
    /**
     * 是否有库存
     */
    private Boolean hasStock;
    /**
     * 热度评分
     */
    private Long hotScore;
    private Long brandId;
    private Long catalogId;
    private String brandName;
    private String brandImg;
    private String catalogName;
    private List<Attr> attrs;

    @Data
    public static class Attr {
        private Long attrId;
        private String attrName;
        private String attrValue;
    }
}




















