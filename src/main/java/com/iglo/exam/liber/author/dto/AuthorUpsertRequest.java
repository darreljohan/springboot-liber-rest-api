package com.iglo.exam.liber.author.dto;

import com.iglo.exam.liber.author.validator.UniqueAuthorName;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class AuthorUpsertRequest {
    @NotNull @Size(min = 1, max = 100)
    private String name;

    @Size(max = 1000)
    private String description;
}
