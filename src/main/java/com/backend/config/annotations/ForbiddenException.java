package com.backend.config.annotations;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.nio.file.AccessDeniedException;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenException extends AccessDeniedException {
    public ForbiddenException() {
        super("Cannot access the resource.");
    }
}
