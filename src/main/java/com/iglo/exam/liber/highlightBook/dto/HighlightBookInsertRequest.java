package com.iglo.exam.liber.highlightBook.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HighlightBookInsertRequest {
    @NotNull
    private Integer orderNumber;
    @NotNull
    private Integer bookId;
}
