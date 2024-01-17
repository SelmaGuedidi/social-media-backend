package com.backend.enums;

import java.util.HashSet;
import java.util.Set;

public enum UserRole {
    USER,
    MODERATOR;

    public static Set<String> getRoles(Set<UserRole> roles) {
        Set<String> roleStrings = new HashSet<>();
        for (UserRole role: roles) {
            roleStrings.add(role.name());
        }
        return roleStrings;
    }
}
