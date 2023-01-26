package com.fosss.gulimall.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import com.fosss.common.valid.MyAnnotation.ListValue;
import com.fosss.common.valid.groups.AddGroup;
import com.fosss.common.valid.groups.UpdateGroup;
import com.fosss.common.valid.groups.UpdateStatusGroup;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.*;

/**
 * 品牌
 *
 * @author fmh
 * @email 1745179058@qq.com
 * @date 2023-01-12 17:50:29
 */
@Data
@TableName("pms_brand")
public class BrandEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 品牌id
     */
    @Null(message = "品牌id必须为空", groups = AddGroup.class)
    @NotNull(message = "品牌id不能为空", groups = UpdateGroup.class)
    @TableId
    private Long brandId;
    /**
     * 品牌名
     */
    //@NotEmpty 用在集合类上面 @NotBlank 用在String上面 @NotNull用在基本类型上
    @NotBlank(message = "品牌名不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String name;
    /**
     * 品牌logo地址
     */
    @NotBlank(message = "品牌logo地址不能为空", groups = AddGroup.class)
    @URL(message = "logo地址必须为一个合法的url", groups = {AddGroup.class, UpdateGroup.class})
    private String logo;
    /**
     * 介绍
     */
    private String descript;
    /**
     * 显示状态[0-不显示；1-显示]
     */
    //使用自定义校验注解
    @ListValue(message = "状态不能为0，1以外的值", vals = {0, 1}, groups = {AddGroup.class, UpdateGroup.class, UpdateStatusGroup.class})
    @NotNull(message = "显示状态不能为空", groups = AddGroup.class)
    private Integer showStatus;
    /**
     * 检索首字母
     */
    @NotBlank(message = "检索首字母不能为空", groups = {AddGroup.class})
    @Pattern(regexp = "^[a-zA-Z]$", message = "检索首字母应为a-z或A-Z的一个字母", groups = {AddGroup.class, UpdateGroup.class})
    private String firstLetter;
    /**
     * 排序
     */
    @Min(value = 0, message = "排序应大于等于零", groups = {AddGroup.class, UpdateGroup.class})
    @NotNull(message = "品牌名不能为空", groups = {AddGroup.class})
    private Integer sort;

}
