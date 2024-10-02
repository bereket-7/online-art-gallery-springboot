package com.project.oag.config.customvalidation;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.regex.Pattern;

import static com.project.oag.common.AppConstants.PHONE_PATTERN;

public class ValidPhoneValidator implements ConstraintValidator<ValidPhone, String> {
    private boolean nullable;

    @Override
    public void initialize(ValidPhone constraintAnnotation) {
        this.nullable = constraintAnnotation.nullable();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return nullable;
        }
        return (value.length() >= 9 && value.length() <= 13 && Pattern.matches(PHONE_PATTERN, value));
    }
}
