package org.pm.authservice.service;

import lombok.RequiredArgsConstructor;
import org.pm.authservice.dto.LoginRequestDto;
import org.pm.authservice.model.User;
import org.pm.authservice.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtl;

    public AuthService(UserService userService, PasswordEncoder passwordEncoder, JwtUtil jwtUtl) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtl = jwtUtl;
    }

    public Optional<String> authenticate(LoginRequestDto loginRequestDto) {
        Optional<String> token = userService.findByEmail(loginRequestDto.getEmail())
                .filter(user ->
                        passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword()))
                .map(u -> jwtUtl.generateToken(u.getEmail(),u.getRole()));
     return token;
    }
}
