package org.event.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.event.anno.State;

public class StateValidation implements ConstraintValidator<State,String> {
    /**
     * 校验规则
     * @param s
     * @param constraintValidatorContext
     * @return //返回false 校验失败，返回true 校验成功
     */
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        //提供校验规则
        if (s==null){
            return false;
        }
        if (s.equals("已发布")||s.equals("草稿")){
            return true;
        }
        return false;
    }
}
