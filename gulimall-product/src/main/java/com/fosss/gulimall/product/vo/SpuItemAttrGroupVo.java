package com.fosss.gulimall.product.vo;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @author: fosss
 * Date: 2023/4/3
 * Time: 21:02
 * Description: spu的规格参数信息
 */
@Data
@ToString
public class SpuItemAttrGroupVo {

    private String groupName;

    private List<Attr> attrs;

}
