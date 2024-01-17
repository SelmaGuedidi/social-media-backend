package com.backend.controllers;

import com.backend.config.annotations.Roles;
import com.backend.entities.Post;
import com.backend.entities.User;
import com.backend.enums.UserRole;
import com.backend.services.UserService;
import com.backend.utils.MessageResponse;
import com.backend.utils.PagingRequest;
import lombok.AllArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
@CrossOrigin("*")
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable String id) {
        User user = null;
        try {
            user = userService.get(id);
            return ResponseEntity.ok(user);
        } catch (ChangeSetPersister.NotFoundException e) {
            return ResponseEntity.status(404).body(new MessageResponse("No user found"));
        }

    }

    @GetMapping("/{id}/posts")
    public Page<Post> getPostsForUser(@PathVariable String id,
                                      @RequestParam String userId,
                                      @ModelAttribute PagingRequest pagingRequest) {
        return userService.getUserPosts(pagingRequest, id, userId);
    }

    @GetMapping("/all/posts")
    public Page<Post> getFriendsPosts(@RequestParam String userId,
                                      @ModelAttribute PagingRequest pagingRequest){
        return userService.getFriendsPosts(pagingRequest, userId);
    }

    @GetMapping("/search")
    public ResponseEntity<List<User>> search(@RequestParam String term) {
        List<User> friends = userService.searchByTerm(term);
        return ResponseEntity.ok(friends);
    }

    @GetMapping("/friends/{id}")
    public ResponseEntity<List<User>> getFriends(@PathVariable String id) {
        List<User> friends = userService.getFriends(id);
        return ResponseEntity.ok(friends);
    }

    @DeleteMapping("/remove-friend/{friendId}")
    public ResponseEntity<User> removeFriend(
            @RequestParam String userId,
            @PathVariable String friendId) {
        User updatedUser = userService.removeFriend(userId, friendId);
        return ResponseEntity.ok(updatedUser);
    }

}
