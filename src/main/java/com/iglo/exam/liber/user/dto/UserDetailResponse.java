package com.iglo.exam.liber.user.dto;

import com.iglo.exam.liber.user.Gender;
import com.iglo.exam.liber.role.RoleName;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class UserDetailResponse {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private Gender gender;
    private Boolean deactivated;
}
