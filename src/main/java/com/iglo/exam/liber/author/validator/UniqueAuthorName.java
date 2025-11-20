package com.iglo.exam.liber.author.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = UniqueAuthorNameValidator.class)
public @interface UniqueAuthorName {
    String message() default "Author name has been taken.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
