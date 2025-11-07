package io.gdpags5.rrs.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = TimeRangeValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidTimeRange {
    String message() default "Time must be within the allowed range";
    String start();
    String end();
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

