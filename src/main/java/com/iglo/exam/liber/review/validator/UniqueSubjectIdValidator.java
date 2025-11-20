package com.iglo.exam.liber.review.validator;

import com.iglo.exam.liber.review.ReadStatus;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;


//TODO : implement validator to check unique subject id via service
@RequiredArgsConstructor
public class UniqueSubjectIdValidator implements ConstraintValidator<NotPlanned, ReadStatus> {

    //private final SubjectService subjectService;

    @Override
    public boolean isValid(ReadStatus readStatus, ConstraintValidatorContext constraintValidatorContext) {

        if(readStatus == ReadStatus.PLANNED) {
            return false;
        };

        return true;
        //return subjectService.findSubjectById(string) == null;
    }
}
