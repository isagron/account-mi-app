package com.isagron.accountmi_api.validations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE,ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = StringMatchesValidator.class)
@Documented
public @interface ValidStringMatch {

    String message() default "String don't match";
    String firstStrFieldName();
    String secondStrFieldName();
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
