package com.backend.dtos.posts;

import com.backend.entities.Tag;
import lombok.*;
import org.bson.types.Binary;
import org.springframework.lang.Nullable;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostDTO {

    private String content;

    private String userId;

    @Getter
    public boolean viewedByAll;

    private List<String> tags;

    @Nullable
    private String[] images;

}
