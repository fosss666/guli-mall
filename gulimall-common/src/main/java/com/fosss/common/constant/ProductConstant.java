package com.fosss.common.constant;

import lombok.Data;

public class ProductConstant {

    public enum ProductStatus {
        SPU_NEW(0, "商品新建"),
        SPU_UP(1, "商品上架"),
        SPU_DOWN(2, "商品下架");
        private int code;
        private String msg;

        ProductStatus(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
    }

    public enum AttrSearchTypeEnum {
        CAN_SEARCH(1, "可以被检索"),
        CANNOT_SEARCH(0, "不可以被检索");
        private int code;
        private String msg;

        AttrSearchTypeEnum(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
    }

    public enum AttrEnum {
        ATTR_TYPE_BASE(1, "基本属性"), ATTR_TYPE_SALE(0, "销售属性");
        private int code;
        private String msg;

        AttrEnum(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
    }
}
