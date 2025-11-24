package com.iglo.exam.liber.auth.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthJwtResponse {
    private String token;
}
