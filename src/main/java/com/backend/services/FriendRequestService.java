package com.backend.services;

import com.backend.entities.FriendRequest;
import com.backend.entities.User;
import com.backend.repositories.FriendRequestRepository;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class FriendRequestService extends GenericService<FriendRequest, FriendRequestRepository>{

    private final UserService userService;

    public FriendRequestService(FriendRequestRepository friendRequestRepository, UserService userService) {
        super(friendRequestRepository);
        this.userService = userService;
    }

    public FriendRequest findRequest(String from, String to){
        FriendRequest p = repository.findFriendRequestByFromAndTo(from, to);
        if (p== null)
            p = repository.findFriendRequestByFromAndTo(to, from);
        return p;
    }

    public List<FriendRequest> getSentRequests(String userId) {
        return repository.findSentRequests(userId);
    }

    public List<FriendRequest> getReceivedRequests(String userId) {
        return repository.findReceivedRequests(userId);
    }

    @SneakyThrows
    public FriendRequest sendRequest(String fromUserId, String toUserId) {
        User fromUser = userService.get(fromUserId);
        User toUser = userService.get(toUserId);

        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setFrom(fromUser);
        friendRequest.setTo(toUser);

        return repository.save(friendRequest);
    }

    @SneakyThrows
    public void deleteRequest(String friendRequestId) {
        FriendRequest friendRequest = this.get(friendRequestId);
        repository.delete(friendRequest);
    }

    @SneakyThrows
    public void acceptRequest(String friendRequestId) {
        FriendRequest req = get(friendRequestId);
        User fromUser = req.getFrom();
        User toUser = req.getTo();

        Set<String> ids = fromUser.getFriendIds();
        ids.add(toUser.getId());
        fromUser.setFriendIds(ids);

        ids = toUser.getFriendIds();
        ids.add(fromUser.getId());
        toUser.setFriendIds(ids);

        userService.create(fromUser);
        userService.create(toUser);

        repository.delete(req);
    }

    public long getCount(String userId){
        return repository.findReceivedRequestsCount(userId);
    }
}
