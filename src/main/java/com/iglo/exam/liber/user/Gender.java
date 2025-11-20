package com.iglo.exam.liber.user;

import lombok.Getter;

@Getter
public enum Gender {
    M("Male"),
    F("Female");

    private final String genderName;

    Gender(String genderName) {
        this.genderName = genderName;
    }

}
