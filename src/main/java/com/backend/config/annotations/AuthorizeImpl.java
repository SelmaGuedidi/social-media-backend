package com.backend.config.annotations;

import com.backend.config.JwtUtils;
import com.backend.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class AuthorizeImpl {

    private final JwtUtils jwtUtils;

    public boolean authorize(HttpServletRequest request, Set<UserRole> roles) {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        jwt = authHeader.substring(7);
        String role = jwtUtils.extractClaim(jwt, claims -> claims.get("role", String.class));
        Set<String> rolesToString = UserRole.getRoles(roles);
        return rolesToString.contains(role);
    }
}