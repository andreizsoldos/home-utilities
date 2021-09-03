package com.home.utilities.validators.index;

import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidatorNumeric implements ConstraintValidator<ValidateNumeric, Double> {

    @Override
    public void initialize(final ValidateNumeric constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(final Double indexValue, final ConstraintValidatorContext context) {
        var validated = true;

        final var hibernateContext = context
              .unwrap(HibernateConstraintValidatorContext.class);

        hibernateContext.disableDefaultConstraintViolation();

        if (isNotNumber(indexValue) && indexValue != null) {
            validated = buildConstraints(hibernateContext, null, null, "{message.index.illegal.value}");
        }

        return validated;
    }

    private boolean isNotNumber(final Double indexValue) {
        if (indexValue != null) {
            return Double.isNaN(indexValue);
        } else {
            return true;
        }
    }

    private boolean buildConstraints(final HibernateConstraintValidatorContext context,
                                     final String parameterName,
                                     final Object parameterObject,
                                     final String message) {
        if (parameterName == null || parameterObject == null) {
            context
                  .buildConstraintViolationWithTemplate(message)
                  .addConstraintViolation();
        } else {
            context
                  .addMessageParameter(parameterName, parameterObject)
                  .buildConstraintViolationWithTemplate(message)
                  .addConstraintViolation();
        }
        return false;
    }
}
