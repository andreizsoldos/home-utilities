package com.home.utilities.validators.index;

import com.home.utilities.services.IndexService;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.ZoneId;

public class ValidatorOncePerDay implements ConstraintValidator<OncePerDay, Object> {

    @Autowired
    private IndexService indexService;

    private String field;
    private String fieldMatch;

    @Override
    public void initialize(final OncePerDay constraintAnnotation) {
        this.field = constraintAnnotation.field();
        this.fieldMatch = constraintAnnotation.fieldMatch();
    }

    @Override
    public boolean isValid(final Object value, final ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();

        final var fieldValue = new BeanWrapperImpl(value).getPropertyValue(field);
        final var fieldMatchValue = new BeanWrapperImpl(value).getPropertyValue(fieldMatch);

        var validated = true;

        if ((fieldValue != null && fieldMatchValue != null) && (isLastIndexCreatedToday((Double) fieldMatchValue))) {
            validated = buildConstraints(context, field, "{message.index.instance}");
        }

        return validated;
    }

    private boolean isLastIndexCreatedToday(final Double lastIndex) {
        final var today = LocalDate.now(ZoneId.systemDefault());
        return indexService.getLastCreatedDate(lastIndex)
              .filter(l -> l.isEqual(today))
              .isPresent();
    }

    private boolean buildConstraints(final ConstraintValidatorContext context, final String applyToField, final String message) {
        context
              .buildConstraintViolationWithTemplate(message)
              .addPropertyNode(applyToField)
              .addConstraintViolation();
        return false;
    }
}
