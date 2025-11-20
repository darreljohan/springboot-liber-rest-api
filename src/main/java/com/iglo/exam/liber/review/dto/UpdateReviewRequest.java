package com.iglo.exam.liber.review.dto;

import com.iglo.exam.liber.review.ReadStatus;
import com.iglo.exam.liber.review.validator.NotPlanned;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@Data
@Builder
public class UpdateReviewRequest {
    @NotNull @NotPlanned
    private ReadStatus readStatus;

    @NotNull @Min(1) @Max(5)
    private Integer rating;

    @NotNull @Size(min=5, max=25)
    private String title;

    @NotNull @Size(min=255, max=8000)
    private String description;

    @NotNull @PastOrPresent
    private LocalDateTime finishedReadingDate;
}
