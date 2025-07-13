package org.pm.authservice.service;

import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.pm.authservice.dto.LoginRequestDto;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class AuthService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserService userService, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public Optional<String> authenticate(LoginRequestDto loginRequestDto) {
        return userService.findByEmail(loginRequestDto.getEmail())
                .filter(user ->
                        passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword()))
                .map(jwtService::generateToken);
    }

    public boolean isTokenValid(String token) {
        try {
            String email = jwtService.extractUsername(token);
            return userService.findByEmail(email)
                    .map(user -> jwtService.isTokenValid(token, user))
                    .orElse(false);
        } catch (JwtException e) {
            // I gue3ss  I shlo
            log.error("Invalid token: {}", e.getMessage());
            return false;
        }
    }
    public Optional<Map<String, Object>> extractClaims(String token) {
        try {
            Map<String, Object> claims = new HashMap<>();
            claims.put("email", jwtService.extractUsername(token));
            claims.put("role", jwtService.extractClaim(token, claim ->claim.get("role", String.class)));
            claims.put("expiration", jwtService.extractExpiration(token));
            return Optional.of(claims);
        } catch (JwtException e) {
            log.error("Failed to extract claims: {}", e.getMessage());
            return Optional.empty();
        }
    }
}
