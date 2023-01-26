package com.fosss.common.valid.MyAnnotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.Set;

/**
 * @author fosss
 * @date 2023/1/26
 * @description：
 */
public class ListValueConstraintValidator implements ConstraintValidator<ListValue,Integer> {
    private Set<Integer> vals=new HashSet<>();
    //初始化方法
    @Override
    public void initialize(ListValue constraintAnnotation) {
        //添加vals
        for (int val : constraintAnnotation.vals()) {
            vals.add(val);
        }

    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        //判断是否符合校验规则
        if(vals.contains(value)){
            return true;
        }
        return false;
    }
}
