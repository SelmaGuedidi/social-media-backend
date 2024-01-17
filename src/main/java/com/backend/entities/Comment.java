package com.backend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
public @Data
class Comment {
    @MongoId
    private String id;

    private String content;
    @DBRef
    private User user;
    @CreatedDate
    private Date createdAt;
}
