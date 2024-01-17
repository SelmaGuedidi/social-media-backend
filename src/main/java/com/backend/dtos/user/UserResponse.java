package com.backend.dtos.user;

import com.backend.entities.Post;
import com.backend.entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    User u;
    List<Post> posts;
    List<Post> shared;
    List<Post> liked;

}
