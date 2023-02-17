package com.fosss.gulimall.ware.vo;

import lombok.Data;

@Data
public class PurchaseItemDoneVo {
    //{itemId:1,status:4,reason:""}
    //采购需求id
    private Long itemId;
    //采购需求状态
    private Integer status;
    //采购失败的原因
    private String reason;
}
