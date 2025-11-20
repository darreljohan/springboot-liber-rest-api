package com.iglo.exam.liber.review.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = UniqueSubjectIdValidator.class)
public @interface NotPlanned {
    String message() default "Read Status cannot be Planned";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
