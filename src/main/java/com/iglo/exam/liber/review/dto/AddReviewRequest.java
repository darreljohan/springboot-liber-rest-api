package com.iglo.exam.liber.review.dto;

import com.iglo.exam.liber.review.ReadStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddReviewRequest {
    @NotNull
    private ReadStatus readStatus;
}
