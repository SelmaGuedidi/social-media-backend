package com.backend.dtos.comments;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CommentDTO {

    private String userId;
    private String postId;
    private String content;
}
