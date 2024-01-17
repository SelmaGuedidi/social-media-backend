package com.backend.services;

import com.backend.dtos.comments.CommentDTO;
import com.backend.dtos.posts.PostDTO;
import com.backend.entities.Comment;
import com.backend.entities.Post;
import com.backend.entities.Tag;
import com.backend.repositories.PostRepository;
import com.backend.repositories.TagRepository;
import com.backend.repositories.UserRepository;
import com.backend.utils.PagingRequest;
import lombok.SneakyThrows;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class PostService extends GenericService<Post, PostRepository>{

    public final CommentService commentService;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;


    public PostService(PostRepository repository, UserRepository userService, CommentService commentService, TagRepository tagRepository) {
        super(repository);
        this.userRepository = userService;
        this.commentService = commentService;
        this.tagRepository = tagRepository;
    }


    @SneakyThrows
    public Post createPost(PostDTO postDTO, MultipartFile[] images) {
        Post post = new Post();
        post.setContent(postDTO.getContent());
        post.setUser(userRepository.findById(postDTO.getUserId()).orElseThrow());
        if (images!= null) {
            List<Binary> im = new ArrayList();
            for (MultipartFile i : images) {
                im.add(new Binary(BsonBinarySubType.BINARY, i.getBytes()));
            }
            post.setImages(im);
        }
        List<Tag> newTags = new ArrayList<>();

        for (String t: postDTO.getTags()){
            Tag x = tagRepository.findByContent(t);
            if (x == null) {
                Tag tag =new Tag(); tag.setContent(t);
                x = tagRepository.save(tag);
            }
            newTags.add(x);
        }
        post.setTags(newTags);
        post.setViewedByAll(postDTO.viewedByAll);
        return repository.save(post);
    }

    @SneakyThrows
    public Post updatePost(PostDTO postDTO, String id, MultipartFile[] images) {
        Post post = this.get(id);
        post.setContent(postDTO.getContent());
        List<Binary> im = new ArrayList<>();
        if (postDTO.getImages() != null)
        for (String i: postDTO.getImages())
            im.add(new Binary(BsonBinarySubType.BINARY, Base64.getDecoder().decode(i)));

        if (images!=null)
        for (MultipartFile i: images) {
            im.add(new Binary(BsonBinarySubType.BINARY, i.getBytes()));
        }

        if (im.size() != 0)
            post.setImages(im);

        List<Tag> newTags = new ArrayList<>();
        for (String t: postDTO.getTags()){
            Tag x = tagRepository.findByContent(t);
            if (x == null) {
                Tag tag =new Tag(); tag.setContent(t);
                x = tagRepository.save(tag);
            }
            newTags.add(x);
        }
        post.setTags(newTags);
        post.setViewedByAll(postDTO.viewedByAll);
        post.setUpdatedAt(new Date());
        return repository.save(post);
    }

    public Page<Post> getAll(PagingRequest req){
        Pageable pageable = this.setPageable(req);
        return repository.findAll(pageable);
    }

    public Page<Post> getByUserId(PagingRequest req, String id){
        Pageable pageable = this.setPageable(req);
        Page<Post> p = repository.findByUserId(id, pageable);
        return p;
    }

    public Page<Post> getViewByAllPosts(PagingRequest req, String id){
        Pageable pageable = this.setPageable(req);
        return repository.findNonFriendsByUserId(id, pageable);
    }

    @SneakyThrows
    public void delete(String id){
        Post p = this.get(id);
        assert p!=null;
        p.setDeleted(true);
        repository.save(p);
    }

    public Page<Post> getSharedById(PagingRequest req, String id){
        Pageable pageable = this.setPageable(req);
        return repository.findBySharedByContains(id, pageable);
    }

    public Page<Post> getLikedById(PagingRequest req, String id){
        Pageable pageable = this.setPageable(req);
        return repository.findByLikedByContains(id, pageable);
    }

    public int getLatestPosts(String userId, String dateString){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/dd/yyyy, h:mm:ss a");
        LocalDate localDate = LocalDate.parse(dateString, formatter);

        Date date = java.sql.Date.valueOf(localDate);

        return repository.countByUserIdAndCreatedAtGreaterThanEqual(userId, date);
    }

    @SneakyThrows
    public Post comment(CommentDTO comment){
        Post p = this.get(comment.getPostId());
        Comment createdComment = commentService.create(comment);
        p.getComments().add(0, createdComment);
        return repository.save(p);
    }

    @SneakyThrows
    public Post likePost(String postId, String userId) {
        Post post = get(postId);
        if (post.getLikedBy().contains(userId))
            post.getLikedBy().remove(userId);
        else post.getLikedBy().add(userId);
        return repository.save(post);
    }

    @SneakyThrows
    public Post sharePost(String postId, String userId) {
        Post post = get(postId);
        if (post.getSharedBy().contains(userId))
            post.getSharedBy().remove(userId);
        else post.getSharedBy().add(userId);
        return repository.save(post);
    }

    public Page<Post> getUsersPostsIn(List<String> stringList, Pageable p){
        return repository.findByUserIdIn(stringList, p);
    }

    public void deleteUserPosts(String userId){
        List<Post> posts = repository.findByUserId(userId);
        for (Post p: posts){
            p.setDeleted(true);
        }
        repository.saveAll(posts);
    }
}

