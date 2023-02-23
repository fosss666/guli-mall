package com.fosss.common.constant;

import lombok.Data;

public class ProductConstant {

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
