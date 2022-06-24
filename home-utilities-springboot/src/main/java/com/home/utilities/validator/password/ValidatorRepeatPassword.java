package com.home.utilities.validator.password;

import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidatorRepeatPassword implements ConstraintValidator<RepeatPassword, Object> {

    private String field;
    private String fieldMatch;

    @Override
    public void initialize(final RepeatPassword constraintAnnotation) {
        this.field = constraintAnnotation.field();
        this.fieldMatch = constraintAnnotation.fieldMatch();
    }

    @Override
    public boolean isValid(final Object value, final ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();

        final var fieldValue = new BeanWrapperImpl(value).getPropertyValue(field);
        final var fieldMatchValue = new BeanWrapperImpl(value).getPropertyValue(fieldMatch);

        var validated = true;

        if (fieldValue != null && fieldMatchValue != null) {
            if (!fieldValue.equals(fieldMatchValue) && (!fieldMatchValue.equals(""))) {
                validated = buildConstraints(context, fieldMatch, "{message.repeatPassword.different}");
            }
        } else {
            validated = buildConstraints(context, fieldMatch, "{message.repeatPassword.null}");
        }

        return validated;
    }

    private boolean buildConstraints(final ConstraintValidatorContext context, final String applyToField, final String message) {
        context
              .buildConstraintViolationWithTemplate(message)
              .addPropertyNode(applyToField)
              .addConstraintViolation();
        return false;
    }
}
