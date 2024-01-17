package com.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.Binary;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document
public @Data
class Post {

    @MongoId
    private String id;
    private String content;
    private List<Binary> images;

    @DBRef
    private User user;

    private Set<String> sharedBy = new HashSet<>();
    private Set<String> likedBy = new HashSet<>();

    @Builder.Default
    private boolean viewedByAll = false;

    @DBRef
    private List<Comment> comments = new ArrayList<>();

    @DBRef
    private List<Tag> tags = new ArrayList<>();

    @CreatedDate
    private Date createdAt;

    private Date updatedAt;

    @JsonIgnore
    @Builder.Default
    private boolean deleted = false;
}
