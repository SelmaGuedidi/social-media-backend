package com.backend.repositories;

import com.backend.entities.AuthUser;
import com.backend.enums.UserRole;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AuthUserRepository extends MongoRepository<AuthUser, String> {
    boolean existsByUsername(String username);

    Optional<AuthUser> findByUsername(String username);

    AuthUser findByUserId(String userId);

    AuthUser findFirstByRole(UserRole role);
}
