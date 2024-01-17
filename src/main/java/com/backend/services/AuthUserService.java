package com.backend.services;

import com.backend.dtos.user.CreateUserDto;
import com.backend.dtos.user.UpdateUserDTO;
import com.backend.entities.AuthUser;
import com.backend.entities.User;
import com.backend.enums.UserRole;
import com.backend.repositories.AuthUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.security.auth.login.CredentialException;
import java.util.Date;
import java.util.Locale;

@RequiredArgsConstructor
@Service
public class AuthUserService {
    private final AuthUserRepository repository;
    private final UserService userService;
    private final PasswordEncoder encoder;

    public AuthUser findByUserId(String id){
        return this.repository.findByUserId(id);
    }

    public String generateUsername(String name) {
        name = name.replaceAll("\\s+", "")
                .toLowerCase(Locale.ROOT);
        int i = 0;
        String username = name;
        while (repository.existsByUsername(username)) {
            i++;
            username = name + i;
        }
        return username;
    }

    @Transactional
    public void delete(String userId){
        AuthUser u = repository.findByUserId(userId);
        u.setDeleted(true);
        repository.save(u);
        userService.delete(userId);
    }

    public User findAdmin() throws ChangeSetPersister.NotFoundException {
        return userService.get(repository.findFirstByRole(UserRole.MODERATOR).getUserId());
    }

    public boolean userExists(String name){
        return repository.existsByUsername(name);
    }

    @Transactional
    public AuthUser createUser(CreateUserDto dto, MultipartFile logo) {
        User u = userService.create(dto, logo);
        AuthUser user = new AuthUser();
        user.setRole(UserRole.USER);
        user.setPassword(encoder.encode(dto.getPassword()));
        user.setUserId(u.getId());
        user.setUsername(dto.getUsername());
        repository.save(user);
        return user;
    }

    public AuthUser findByUsername(String username) throws ChangeSetPersister.NotFoundException {
        return repository.findByUsername(username).orElseThrow(ChangeSetPersister.NotFoundException::new);
    }

    public void update(UpdateUserDTO dto, String username, MultipartFile logo) throws ChangeSetPersister.NotFoundException, CredentialException{
        AuthUser u = this.findByUserId(username);
            userService.update(username, dto, logo);
            if (dto.getNewPassword() != null && !dto.getNewPassword().isEmpty())
             u.setPassword(encoder.encode(dto.getNewPassword()));

            repository.save(u);

    }

    public void update(AuthUser u) {
        u.setLastLogIn(new Date());
        repository.save(u);
    }

}
