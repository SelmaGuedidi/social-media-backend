package com.backend.controllers;

import com.backend.entities.FriendRequest;
import com.backend.services.FriendRequestService;
import com.backend.utils.MessageResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/friend-requests")
@AllArgsConstructor
@CrossOrigin("*")
public class FriendRequestController {

    private final FriendRequestService friendRequestService;

    @PostMapping("/send/{id}")
    public ResponseEntity<FriendRequest> sendFriendRequest(
            @RequestParam String userId,
            @PathVariable String id) {
        FriendRequest friendRequest = friendRequestService.sendRequest(userId, id);
        return ResponseEntity.status(HttpStatus.CREATED).body(friendRequest);
    }

    @DeleteMapping("/delete/{friendRequestId}")
    public ResponseEntity<Void> deleteFriendRequest(@PathVariable String friendRequestId) {
        friendRequestService.deleteRequest(friendRequestId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/accept/{friendRequestId}")
    public ResponseEntity<Void> acceptFriendRequest(@PathVariable String friendRequestId) {
        friendRequestService.acceptRequest(friendRequestId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<FriendRequest> find(@PathVariable String id, @RequestParam String userId) {
        return ResponseEntity.ok(friendRequestService.findRequest(userId, id));
    }

    @GetMapping("/sent")
    public ResponseEntity<List<FriendRequest>> getSentFriendRequests(@RequestParam String userId) {
        List<FriendRequest> sentRequests = friendRequestService.getSentRequests(userId);
        return ResponseEntity.ok(sentRequests);
    }

    @GetMapping("/received")
    public ResponseEntity<List<FriendRequest>> getReceivedFriendRequests(@RequestParam String userId) {
        List<FriendRequest> receivedRequests = friendRequestService.getReceivedRequests(userId);
        return ResponseEntity.ok(receivedRequests);
    }

    @GetMapping("/received/count")
    public ResponseEntity<MessageResponse> getReceivedFriendRequestsCount(@RequestParam String userId) {
        long c = friendRequestService.getCount(userId);
        return ResponseEntity.ok(new MessageResponse(Long.toString(c)));
    }
}
