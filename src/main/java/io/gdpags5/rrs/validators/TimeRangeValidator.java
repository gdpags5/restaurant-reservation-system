package io.gdpags5.rrs.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class TimeRangeValidator implements ConstraintValidator<ValidTimeRange, LocalTime> {

    private LocalTime startTime;
    private LocalTime endTime;

    @Override
    public void initialize(ValidTimeRange constraintAnnotation) {
        try {
            startTime = LocalTime.parse(constraintAnnotation.start());
            endTime = LocalTime.parse(constraintAnnotation.end());
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid time format in @ValidTimeRange");
        }
    }

    @Override
    public boolean isValid(LocalTime value, ConstraintValidatorContext context) {
        return value != null && !value.isBefore(startTime) && !value.isAfter(endTime);
    }
}

