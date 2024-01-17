package com.backend.repositories;

import com.backend.entities.FriendRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface FriendRequestRepository extends MongoRepository<FriendRequest, String> {

    @Query(value = "{ 'from._id': ?0 }", fields = "{ to : 1 }")
    List<FriendRequest> findSentRequests(String id);

    @Query(value = "{ 'to._id': ?0 }", fields = "{ from : 1 }")
    List<FriendRequest> findReceivedRequests(String id);

    @Query(value = "{ 'to._id': ?0 }", count = true)
    long findReceivedRequestsCount(String id);

    @Query(value = "{ 'from._id': ?0, 'to._id': ?1}")
    FriendRequest findFriendRequestByFromAndTo(String from, String to);
}
