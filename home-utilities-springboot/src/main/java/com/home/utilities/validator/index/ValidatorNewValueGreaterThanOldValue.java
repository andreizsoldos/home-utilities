package com.home.utilities.validator.index;

import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidatorNewValueGreaterThanOldValue implements ConstraintValidator<NewValueGreaterThanOldValue, Object> {

    private String field;
    private String fieldMatch;

    @Override
    public void initialize(final NewValueGreaterThanOldValue constraintAnnotation) {
        this.field = constraintAnnotation.field();
        this.fieldMatch = constraintAnnotation.fieldMatch();
    }

    @Override
    public boolean isValid(final Object value, final ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();

        final var fieldValue = new BeanWrapperImpl(value).getPropertyValue(field);
        final var fieldMatchValue = new BeanWrapperImpl(value).getPropertyValue(fieldMatch);

        var validated = true;

        if ((fieldValue != null && fieldMatchValue != null) && (((Double) fieldValue) < ((Double) fieldMatchValue))) {
            validated = buildConstraints(context, field, "{message.index.smaller}");
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
