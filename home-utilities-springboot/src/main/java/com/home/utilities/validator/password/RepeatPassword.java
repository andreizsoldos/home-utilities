package com.home.utilities.validator.password;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidatorRepeatPassword.class)
@Target({ElementType.TYPE})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface RepeatPassword {
    String message() default "{}";

    String field();

    String fieldMatch();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
