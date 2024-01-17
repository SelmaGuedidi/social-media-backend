package com.backend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
public @Data
class FriendRequest {
    @MongoId
    private String id;
    @DBRef
    private User from;
    @DBRef
    private User to;
    @CreatedDate
    private Date createdAt;
}
