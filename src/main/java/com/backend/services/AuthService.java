package com.backend.services;

import com.backend.config.JwtUtils;
import com.backend.dtos.auth.AuthRequest;
import com.backend.dtos.auth.AuthenticationResponse;
import com.backend.dtos.user.CreateUserDto;
import com.backend.dtos.user.UpdateUserDTO;
import com.backend.entities.AuthUser;
import com.backend.entities.User;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.security.auth.login.CredentialException;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthUserService userService;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authManager;

    public void delete(String id){
        userService.delete(id);
    }

    public User getAdmin() throws ChangeSetPersister.NotFoundException {
        return userService.findAdmin();
    }


    public String generateUsername(String username){
        return userService.generateUsername(username);
    }

    @SneakyThrows
    public AuthenticationResponse updateUser(UpdateUserDTO dto, String username, MultipartFile logo) throws CredentialException {
        try {
            userService.update(dto, username, logo);
        } catch (ChangeSetPersister.NotFoundException e) {
            e.printStackTrace();
        } catch (CredentialException e) {
            throw new CredentialException();
        }
        AuthUser u = userService.findByUserId(username);
        return authenticate(new AuthRequest(u.getUsername(), dto.getNewPassword()));
    }

    public boolean userExists(String name){
        return userService.userExists(name);
    }

    public AuthenticationResponse register(CreateUserDto request, MultipartFile logo) {
        var user = userService.createUser(request, logo);
        var token = jwtUtils.generateToken(user, user);
        return  AuthenticationResponse.builder()
                .token(token)
                .build();
    }

    @SneakyThrows
    public AuthenticationResponse authenticate(AuthRequest request) throws BadCredentialsException, IllegalAccessException{
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        AuthUser user = userService.findByUsername(request.getUsername());

        if (user.isDeleted()) throw new IllegalAccessException();
        var token = jwtUtils.generateToken(user, user);
        userService.update(user);
        return  AuthenticationResponse.builder()
                .token(token)
                .build();
    }
}
