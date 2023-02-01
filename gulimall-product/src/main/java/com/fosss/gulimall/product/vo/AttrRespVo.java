package com.fosss.gulimall.product.vo;

import lombok.Data;

/**
 * @author fosss
 * @date 2023/1/31
 * @description：
 */
@Data
public class AttrRespVo extends AttrVo{
    /**
     * 分类名称
     */
    private String catelogName;
    /**
     * 分组名称
     */
    private String groupName;
    /**
     * 分类路径
     */
    private Long[] catelogPath;
}












