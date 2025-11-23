package com.iglo.exam.liber.role;

import lombok.Getter;

@Getter
public enum RoleName {
    USER("USER"),
    ADMIN("ADMIN");

    private String roleLabel;

    RoleName(String roleLabel) {
        this.roleLabel = roleLabel;
    }
}
