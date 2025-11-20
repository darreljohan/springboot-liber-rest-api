package com.iglo.exam.liber.author.validator;

import com.iglo.exam.liber.author.AuthorService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class UniqueAuthorNameValidator implements ConstraintValidator<UniqueAuthorName, String> {

    private final AuthorService authorService;

    @Override
    public boolean isValid(String string, ConstraintValidatorContext context) {

        if(string == null){
            return true;
        };

        boolean exists = authorService.isAuthorExistByName(string);

        if (exists) {
            // Disable default message
            context.disableDefaultConstraintViolation();

            // Build custom message with the actual name
            context.buildConstraintViolationWithTemplate(
                    "Author name '" + string + "' has been taken."
            ).addConstraintViolation();
        }

        return !exists;
    }
}
