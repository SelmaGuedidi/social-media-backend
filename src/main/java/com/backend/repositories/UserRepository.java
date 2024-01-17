package com.backend.repositories;

import com.backend.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {

    @Query(value = "{ '_id': { $in: ?0 }, 'deleted': false }")
    List<User> findByIdsIn(List<String> ids);

    @Query(value = "{ $or: [ { 'firstName': { $regex: ?0, $options: 'i' } }, { 'lastName': { $regex: ?1, $options: 'i' } } ], 'deleted': false }")
    List<User> findByName(String firstname, String lastname);

}
