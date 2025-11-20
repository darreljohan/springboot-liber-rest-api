package com.iglo.exam.liber.highlightBook.dto;

import com.iglo.exam.liber.book.dto.BookResponse;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class HighlightBookResponse {
    private Integer orderNumber;
    private BookResponse book;
    private LocalDateTime addedDate;
}
