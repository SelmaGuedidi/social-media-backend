package com.backend.services;

import com.backend.dtos.comments.CommentDTO;
import com.backend.entities.Comment;
import com.backend.entities.User;
import com.backend.repositories.CommentRepository;
import com.backend.repositories.UserRepository;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
public class CommentService extends GenericService<Comment, CommentRepository> {

    private final UserRepository userService;
    public CommentService(CommentRepository repository, UserRepository userService) {
        super(repository);
        this.userService = userService;
    }

    @SneakyThrows
    public Comment create(CommentDTO dto){
        Comment c = new Comment();
        c.setContent(dto.getContent());
        User u = userService.findById(dto.getUserId()).orElseThrow();
        c.setUser(u);
        return repository.save(c);
    }
}
