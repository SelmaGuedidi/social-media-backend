package com.backend.repositories;

import com.backend.entities.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Date;
import java.util.List;

public interface PostRepository extends MongoRepository<Post, String> {
    @Query(value = "{ 'user._id': ?0, 'deleted': false }")
    Page<Post> findByUserId(String id, Pageable pageable);

    @Query(value = "{ 'user.$id': { $in: ?0 }, 'deleted': false }")
    Page<Post> findByUserIdIn(List<String> ids, Pageable pageable);

    @Query(value = "{ 'user._id': ?0, 'deleted': false, 'viewedByAll': true }")
    Page<Post> findNonFriendsByUserId(String id, Pageable pageable);

    @Query(value = "{ 'sharedBy': ?0, 'deleted': false }")
    Page<Post> findBySharedByContains(String id, Pageable pageable);

    @Query(value = "{ 'likedBy': ?0, 'deleted': false }")
    Page<Post> findByLikedByContains(String id, Pageable pageable);

    @Query(value = "{ 'deleted': false }")
    Page<Post> findAll(Pageable pageable);

    List<Post> findByUserId(String id);

    int countByUserIdAndCreatedAtGreaterThanEqual(String userId, Date date);
}
