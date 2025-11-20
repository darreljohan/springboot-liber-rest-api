package com.iglo.exam.liber.book.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@Builder
public class BookUpsertRequest {
    @NotNull @Size(max = 100)
    private String name;

    @NotNull @Size(max = 1000)
    private String cover;

    @NotNull @PastOrPresent @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate releaseDate;

    @NotNull
    private Integer authorId;
}
