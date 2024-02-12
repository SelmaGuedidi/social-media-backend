package com.backend.controllers;

import com.backend.config.annotations.Roles;
import com.backend.dtos.auth.AuthRequest;
import com.backend.dtos.user.CreateUserDto;
import com.backend.dtos.user.UpdateUserDTO;
import com.backend.entities.User;
import com.backend.enums.UserRole;
import com.backend.services.AuthService;
import com.backend.utils.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.security.auth.login.CredentialException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:4200")
public class AuthController {

    private final AuthService service;

    @GetMapping(value = "/get-username")
    public ResponseEntity<?> generateUsername(
            @RequestParam(name = "name") String name
    ) {
        try {
            return ResponseEntity.ok(new MessageResponse(service.generateUsername(name)));
        } catch(Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("An error occurred please try again."));
        }
    }

    @GetMapping(value = "/check-username")
    public ResponseEntity<?> checkUsername(
            @RequestParam(name = "name") String name
    ) {
        try {
            boolean b = service.userExists(name);
            if (b)
            return ResponseEntity.status(409).body(new MessageResponse("Username already exists"));
            return ResponseEntity.ok(new MessageResponse("ok"));
        } catch(Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("An error occurred please try again."));
        }
    }

    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> register(
            @ModelAttribute CreateUserDto request,
            @Nullable @RequestPart(value = "profilePicture", required = false) MultipartFile logo
    ) {
        try {
            return ResponseEntity.ok(service.register(request, logo));
        } catch(Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("An error occurred please try again."));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody AuthRequest request
    ) {
        try {
            return ResponseEntity.ok(service.authenticate(request));
        } catch (UsernameNotFoundException ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("This account doesn't exist"));
        } catch(AuthenticationException ex){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Please check your username and password again"));
        } catch (IllegalAccessException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("This Account has been banned for violating our guidelines."));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.status(200).body(new MessageResponse("Deleted successfully"));
    }

    @GetMapping("/admin")
    public ResponseEntity<?> getAdmin() {
        User user = null;
        try {
            user = service.getAdmin();
            return ResponseEntity.ok(user);
        } catch (ChangeSetPersister.NotFoundException e) {
            return ResponseEntity.status(404).body(new MessageResponse("No user found"));
        }
    }

    @PostMapping(value = "/update-account", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> update(
            @ModelAttribute UpdateUserDTO request,
            @RequestParam String userId,
            @Nullable @RequestPart(value = "profilePicture", required = false) MultipartFile logo
    ) {
        try {
            System.out.println(logo);
            return ResponseEntity.ok(service.updateUser(request, userId, logo));
        } catch(BadCredentialsException x) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Old password doesn't match"));
        }
        catch(Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("An error occurred please try again."));
        }
    }

}
