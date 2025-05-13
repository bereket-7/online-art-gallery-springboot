package com.project.oag.config.customvalidation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TwoDecimalPositiveNumberValidator.class)
public @interface TwoDecimalPositiveNumber {

    String message() default "Invalid format. Only two decimal places and positive numbers are allowed.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}