package com.backend.config.annotations;

import com.backend.enums.UserRole;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Roles {
    UserRole[] value();
}