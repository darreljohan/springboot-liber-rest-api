package com.iglo.exam.liber.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private Boolean deactivated;
}
