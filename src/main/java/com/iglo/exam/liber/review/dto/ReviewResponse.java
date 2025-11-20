package com.iglo.exam.liber.review.dto;

import com.iglo.exam.liber.book.dto.BookResponse;
import com.iglo.exam.liber.user.dto.UserResponse;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class ReviewResponse {
    private String title;
    private String description;
    private LocalDate addedDate;
    private String readStatus;
    private LocalDate finishedReadingDate;
    private LocalDateTime reviewDate;
    private UserResponse user;
    private BookResponse book;
}
