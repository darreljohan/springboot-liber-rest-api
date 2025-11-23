package com.iglo.exam.liber.user.dto;

import com.iglo.exam.liber.role.RoleName;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthRegisterRequest {
    private final String username;
    private final String password;
    @NotNull
    private final RoleName role;
}
