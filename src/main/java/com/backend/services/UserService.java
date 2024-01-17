package com.backend.services;


import com.backend.dtos.user.CreateUserDto;
import com.backend.dtos.user.UpdateUserDTO;
import com.backend.entities.Post;
import com.backend.entities.User;
import com.backend.repositories.UserRepository;
import com.backend.utils.PagingRequest;
import lombok.SneakyThrows;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class UserService extends GenericService<User, UserRepository> {

    private final PostService postService;

    public UserService(UserRepository repository, PostService service) {
        super(repository);
        postService = service;
    }

    @SneakyThrows
    public User create(CreateUserDto dto, MultipartFile profilePicture) {
        User user = new User();
        String dateString = (dto.getDateOfBirth());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setDateOfBirth(dateFormat.parse(dateString));
        user.setEmail(dto.getEmail());

        if (profilePicture != null)
            user.setProfilePicture(
                    new Binary(BsonBinarySubType.BINARY, profilePicture.getBytes()));

        return repository.save(user);
    }


    @SneakyThrows
    public User update(String id, UpdateUserDTO dto, MultipartFile logo) throws NotFoundException {
        User user = this.get(id);
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        String dateString = (dto.getDateOfBirth());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            user.setDateOfBirth(dateFormat.parse(dateString));
        } catch (ParseException ignored){}
        if (dto.getOldImage() == null)
            user.setProfilePicture(null);
        if (logo != null)
            user.setProfilePicture(
                    new Binary(BsonBinarySubType.BINARY, logo.getBytes()));
        return repository.save(user);
    }

    public List<User> searchByTerm(String term) {
        return repository.findByName(term, term);
    }

    @SneakyThrows
    public List<User> getFriends(String id){
        Set<String> ids = this.get(id).getFriendIds();
        System.out.println(ids);
        return repository.findByIdsIn(new ArrayList<>(ids));
    }

    @SneakyThrows
    public User removeFriend(String userId, String friendId){
        User u1 = this.get(friendId);
        Set<String> ids1 = u1.getFriendIds();
        ids1.remove(userId);
        u1.setFriendIds(ids1);

        User u = this.get(userId);
        Set<String> ids = u.getFriendIds();
        ids.remove(friendId);
        u.setFriendIds(ids);

        repository.save(u);
        repository.save(u1);

        return u;
    }

    @SneakyThrows
    public Page<Post> getUserPosts(PagingRequest req, String requestedId, String myId){
        User u = this.get(myId);
        assert u!= null;
        if (Objects.equals(myId, requestedId) || u.getFriendIds().contains(requestedId))
            return postService.getByUserId(req, requestedId);
        else
            return postService.getViewByAllPosts(req, requestedId);
    }

    @SneakyThrows
    public Page<Post> getFriendsPosts(PagingRequest req, String id){
        User u = this.get(id);
        assert u!=null;
        List<String> stringList = new ArrayList<>(u.getFriendIds());
        stringList.add(id);
        return postService.getUsersPostsIn(stringList, setPageable(req));
    }

    @SneakyThrows
    @Transactional
    public void delete(String id) {
        User u = this.get(id);
        u.setDeleted(true);
        repository.save(u);
        postService.deleteUserPosts(id);
    }
}
