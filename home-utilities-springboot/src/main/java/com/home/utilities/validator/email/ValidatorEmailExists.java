package com.home.utilities.validator.email;

import com.home.utilities.service.UserService;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidatorEmailExists implements ConstraintValidator<EmailExists, String> {

    @Autowired
    private UserService userService;

    @Override
    public void initialize(final EmailExists constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(final String email, final ConstraintValidatorContext context) {
        var validated = true;

        final var hibernateContext = context
              .unwrap(HibernateConstraintValidatorContext.class);

        hibernateContext.disableDefaultConstraintViolation();

        if (!email.equals("") && emailExists(email)) {
            validated = buildConstraints(hibernateContext, null, null, "{message.email.exists}");
        }
        return validated;
    }

    private boolean emailExists(final String email) {
        return userService.existsEmail(email);
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
