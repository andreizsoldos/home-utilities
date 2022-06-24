package com.home.utilities.validator.index;

import com.home.utilities.configuration.userdetails.UserPrincipal;
import com.home.utilities.entity.Branch;
import com.home.utilities.service.IndexService;
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
    private String fieldMatchClientId;
    private String fieldMatchBranch;

    @Override
    public void initialize(final OncePerDay constraintAnnotation) {
        this.field = constraintAnnotation.field();
        this.fieldMatch = constraintAnnotation.fieldMatch();
        this.fieldMatchClientId = constraintAnnotation.fieldMatchClientId();
        this.fieldMatchBranch = constraintAnnotation.fieldMatchBranch();
    }

    @Override
    public boolean isValid(final Object value, final ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();

        final var fieldValue = new BeanWrapperImpl(value).getPropertyValue(field);
        final var fieldMatchValue = new BeanWrapperImpl(value).getPropertyValue(fieldMatch);
        final var fieldMatchClientIdValue = new BeanWrapperImpl(value).getPropertyValue(fieldMatchClientId);
        final var fieldMatchBranchValue = new BeanWrapperImpl(value).getPropertyValue(fieldMatchBranch);

        var validated = true;

        if ((fieldValue != null && fieldMatchValue != null) && (isLastIndexCreatedToday((Double) fieldMatchValue, (Long) fieldMatchClientIdValue, (Branch) fieldMatchBranchValue))) {
            validated = buildConstraints(context, field, "{message.index.instance}");
        }

        return validated;
    }

    private boolean isLastIndexCreatedToday(final Double lastIndex, final Long clientId, final Branch branch) {
        final var today = LocalDate.now(ZoneId.systemDefault());
        final var userId = UserPrincipal.getCurrentUser().getId();

        return indexService.getLastCreatedIndexDate(lastIndex, clientId, branch, userId)
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
