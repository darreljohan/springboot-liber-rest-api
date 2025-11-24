package com.iglo.exam.liber.auth.dto;

import lombok.Data;

@Data
public class AuthJwtRequest {
    private String username;
    private String password;
}
