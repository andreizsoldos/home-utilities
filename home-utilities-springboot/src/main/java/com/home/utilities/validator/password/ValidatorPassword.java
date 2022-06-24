package com.home.utilities.validator.password;

import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidatorPassword implements ConstraintValidator<ValidatePassword, String> {

    private static final int MIN_LENGTH = 6;

    @Override
    public void initialize(final ValidatePassword constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(final String password, final ConstraintValidatorContext context) {
        var validated = true;

        final var hibernateContext = context
              .unwrap(HibernateConstraintValidatorContext.class);

        hibernateContext.disableDefaultConstraintViolation();

        if (!password.equals("") && (password.length() < MIN_LENGTH)) {
            validated = buildConstraints(hibernateContext, "min", MIN_LENGTH, "{size.registerRequest.password}");
        }

        return validated;
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
