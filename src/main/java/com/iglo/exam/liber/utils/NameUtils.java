package com.iglo.exam.liber.utils;

import java.util.Arrays;

public class NameUtils {

    public static String[] splitFullName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            return new String[]{"", ""};
        }
        String[] nameParts = fullName.trim().split("\\s+");
        String firstName = nameParts[0];

        StringBuilder lastNameBuilder = new StringBuilder();
        for (int i = 1; i < nameParts.length; i++) {
            lastNameBuilder.append(nameParts[i]);
        }

        String lastName = lastNameBuilder.toString();
        return new String[]{firstName, lastName};
    }
}
