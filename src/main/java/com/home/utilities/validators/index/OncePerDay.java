package com.home.utilities.validators.index;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidatorOncePerDay.class)
@Target({ElementType.TYPE})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface OncePerDay {
    String message() default "{}";

    String field();

    String fieldMatch();

    String fieldMatchClientId();

    String fieldMatchBranch();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
