package com.backend.entities;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
public @Data
class Tag {
    private String content;
    private String id;
}
