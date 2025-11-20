package com.iglo.exam.liber.book.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class BookResponse {
    private Integer id;
    private String name;
    private String cover;
    private LocalDate releaseDate;
    private String authorName;
}
