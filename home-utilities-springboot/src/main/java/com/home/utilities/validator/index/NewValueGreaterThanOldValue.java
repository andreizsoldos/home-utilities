package com.home.utilities.validator.index;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidatorNewValueGreaterThanOldValue.class)
@Target({ElementType.TYPE})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface NewValueGreaterThanOldValue {
    String message() default "{}";

    String field();

    String fieldMatch();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
