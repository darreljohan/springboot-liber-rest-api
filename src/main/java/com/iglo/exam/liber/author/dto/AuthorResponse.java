package com.iglo.exam.liber.author.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthorResponse {
    private Integer id;
    private String name;
    private String description;
}
