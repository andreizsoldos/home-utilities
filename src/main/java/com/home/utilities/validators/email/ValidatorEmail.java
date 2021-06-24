package com.home.utilities.validators.email;

import com.home.utilities.services.UserService;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidatorEmail implements ConstraintValidator<ValidateEmail, String> {

    private static final String DUPLICATE_AT_SYMBOL = "^(?=(?:[^@]*@){2,}).*";
    private static final String SPACE_FOUND = "^(?=[\\S]*\s).*";
    private static final String FIRST_LETTER_VALID = "^[a-zA-Z].*";
    private static final String DOMAIN_VALID = "^(?=[^@]*@[a-zA-Z0-9]{2,}).*(?:\\.[a-zA-Z]{2,8})(?=$)";
    private static final String ILLEGAL_CONSECUTIVE_DOTS = "^(?=[^\\.]*\\.\\.).*";
    private static final int MAX_LENGTH = 50;

    @Autowired
    private UserService userService;

    @Override
    public void initialize(final ValidateEmail constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(final String email, final ConstraintValidatorContext context) {
        var validated = true;

        final var hibernateContext = context
              .unwrap(HibernateConstraintValidatorContext.class);

        hibernateContext.disableDefaultConstraintViolation();

        if (!email.equals("")) {
            if (emailExists(email)) {
                validated = buildConstraints(hibernateContext, null, null, "{message.email.exists}");
            }
            if (email.length() > MAX_LENGTH) {
                validated = buildConstraints(hibernateContext, "max", MAX_LENGTH, "{size.registerRequest.email}");
            }
            if (validate(email, DUPLICATE_AT_SYMBOL)) {
                validated = buildConstraints(hibernateContext, null, null, "{message.email.duplicateAtSymbol}");
            }
            if (validate(email, SPACE_FOUND)) {
                validated = buildConstraints(hibernateContext, null, null, "{message.email.spaceNotAllowed}");
            }
            if (!validate(email, FIRST_LETTER_VALID)) {
                validated = buildConstraints(hibernateContext, null, null, "{message.email.illegalFirstLetter}");
            }
            if (!validate(email, DOMAIN_VALID)) {
                validated = buildConstraints(hibernateContext, null, null, "{message.email.illegalDomain}");
            }
            if (validate(email, ILLEGAL_CONSECUTIVE_DOTS)) {
                validated = buildConstraints(hibernateContext, null, null, "{message.email.illegalConsecutiveDots}");
            }
        }
        return validated;
    }

    private boolean validate(final String email, final String pattern) {
        return email.matches(pattern);
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
