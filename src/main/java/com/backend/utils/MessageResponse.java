package com.backend.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MessageResponse {
    String message;

    public MessageResponse() {
        this.message = "An error occurred, please try again later.";
    }
}
