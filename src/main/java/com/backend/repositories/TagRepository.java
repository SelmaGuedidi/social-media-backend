package com.backend.repositories;

import com.backend.entities.Tag;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends MongoRepository<Tag, String> {
    public boolean existsByContent(String content);

    Tag findByContent(String content);
}
