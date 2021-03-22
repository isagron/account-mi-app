package com.isagron.accountmi_api.validations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;

public class StringMatchesValidator implements ConstraintValidator<ValidStringMatch, Object> {

    private String firstFieldName;

    private String secondFieldName;

    @Override
    public void initialize(ValidStringMatch constraintAnnotation) {
        this.firstFieldName = constraintAnnotation.firstStrFieldName();
        this.secondFieldName = constraintAnnotation.secondStrFieldName();
    }
    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context){
        try {
            Field firstField = obj.getClass().getField(firstFieldName);
            Field secondField = obj.getClass().getField(secondFieldName);
            Object firstValue = firstField.get(obj);
            Object secondValue = secondField.get(obj);
            return firstValue.equals(secondValue);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return false;
        }

    }
}
