package com.backend.controllers;
import com.backend.config.annotations.Roles;
import com.backend.dtos.comments.CommentDTO;
import com.backend.dtos.posts.PostDTO;
import com.backend.entities.Comment;
import com.backend.entities.Post;
import com.backend.entities.User;
import com.backend.enums.UserRole;
import com.backend.services.PostService;
import com.backend.utils.MessageResponse;
import com.backend.utils.PagingRequest;
import lombok.AllArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/posts")
@AllArgsConstructor
@CrossOrigin("*")
public class PostController {

    private final PostService postService;

    @PostMapping("/comment")
    public ResponseEntity<Post> createComment(@RequestBody CommentDTO commentDTO, @RequestParam String userId) {
        System.out.println(userId);
        commentDTO.setUserId(userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.comment(commentDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPost(@PathVariable String id) {
        Post user = null;
        try {
            user = postService.get(id);
            return ResponseEntity.ok(user);
        } catch (ChangeSetPersister.NotFoundException e) {
            return ResponseEntity.status(404).body(new MessageResponse("No user found"));
        }
    }

    @Roles(UserRole.MODERATOR)
    @GetMapping("/admin/all")
    public Page<Post> getAllPosts(@ModelAttribute PagingRequest pagingRequest) {
        return postService.getAll(pagingRequest);
    }

    @Roles(UserRole.MODERATOR)
    @GetMapping("/admin/user/{userId}")
    public Page<Post> getPostsByUserId(@PathVariable String userId,
                                       @ModelAttribute PagingRequest pagingRequest) {
        return postService.getByUserId(pagingRequest, userId);
    }


    @GetMapping("/shared/{userId}")
    public Page<Post> getSharedPostsById(@PathVariable String userId,
                                         @ModelAttribute PagingRequest pagingRequest) {
        return postService.getSharedById(pagingRequest, userId);
    }

    @GetMapping("/liked/{userId}")
    public Page<Post> getLikedPostsById(@PathVariable String userId,
                                        @ModelAttribute PagingRequest pagingRequest) {
        return postService.getLikedById(pagingRequest, userId);
    }


    @DeleteMapping("/{postId}")
    public void deletePost(@PathVariable String postId) {
        postService.delete(postId);
    }

    @PostMapping(value="/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Post> createPost(@ModelAttribute PostDTO postDTO,
                                           @RequestParam String userId,
                                           @Nullable @RequestPart(value = "newImages", required = false) MultipartFile[] images) {
        postDTO.setUserId(userId);
        Post createdPost = postService.createPost(postDTO, images);
        return ResponseEntity.ok(createdPost);
    }

    @PutMapping(value = "/update/{postId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Post> updatePost(@PathVariable String postId, @ModelAttribute PostDTO postDTO,
                                           @RequestParam String userId,
                                           @Nullable @RequestPart(value = "newImages", required = false) MultipartFile[] images) {
        postDTO.setUserId(userId);
        Post updatedPost = postService.updatePost(postDTO, postId, images);
        return ResponseEntity.ok(updatedPost);
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<Post> likePost(@PathVariable String postId, @RequestParam String userId) {

        return ResponseEntity.ok(postService.likePost(postId, userId));
    }

    @PostMapping("/{postId}/share")
    public ResponseEntity<Post> sharePost(@PathVariable String postId, @RequestParam String userId) {

        return ResponseEntity.ok(postService.sharePost(postId, userId));
    }

    @GetMapping("/{userId}/count")
    public ResponseEntity<MessageResponse> countLatestPosts(@PathVariable String userId, @RequestParam String date){
        return ResponseEntity.ok(new MessageResponse(String.valueOf(postService.getLatestPosts(userId, date))));
    }
}
