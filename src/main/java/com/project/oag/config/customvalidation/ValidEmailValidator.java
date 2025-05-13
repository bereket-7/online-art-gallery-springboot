package com.project.oag.config.customvalidation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

import static com.project.oag.common.AppConstants.EMAIL_PATTERN;

public class ValidEmailValidator implements ConstraintValidator<ValidEmail, String> {

    private boolean nullable;

    @Override
    public void initialize(ValidEmail constraintAnnotation) {
        this.nullable = constraintAnnotation.nullable();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return nullable;
        }
        return Pattern.matches(EMAIL_PATTERN, value);
    }
}