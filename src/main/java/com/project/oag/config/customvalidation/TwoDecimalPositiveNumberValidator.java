package com.project.oag.config.customvalidation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.math.BigDecimal;

public class TwoDecimalPositiveNumberValidator implements ConstraintValidator<TwoDecimalPositiveNumber, BigDecimal> {

    @Override
    public void initialize(TwoDecimalPositiveNumber constraintAnnotation) {
    }

    @Override
    public boolean isValid(BigDecimal value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // null values are considered valid
        }
        if (value.compareTo(BigDecimal.ZERO) < 1) {
            return false;
        }
        // Check if the BigDecimal has exactly two decimal places
        return value.scale() <= 2;
    }
}