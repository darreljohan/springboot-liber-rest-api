package com.iglo.exam.liber.review.dto;

import com.iglo.exam.liber.review.ReadStatus;
import com.iglo.exam.liber.review.validator.NotPlanned;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AddRatingRequest {
    @NotNull @NotPlanned
    private ReadStatus readStatus;

    @NotNull @Max(5) @Min(1)
    private Integer rating;

    @NotNull @PastOrPresent
    private LocalDateTime finishedReadingDate;
}
