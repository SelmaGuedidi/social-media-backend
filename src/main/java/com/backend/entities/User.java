package com.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.Binary;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document
public @Data
class User {

    @MongoId
    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private Binary profilePicture;
    private Date dateOfBirth;

    @DocumentReference(lookup = "{userId : ?#{#self._id}}")
    @JsonIgnore
    AuthUser user;

    Set<String> friendIds = new HashSet<>();

    @Builder.Default
    private boolean deleted = false;

    public boolean getDeleted() {
        return deleted;
    }
}
