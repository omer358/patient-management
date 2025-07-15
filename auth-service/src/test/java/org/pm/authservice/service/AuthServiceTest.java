package org.pm.authservice.service;

import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pm.authservice.dto.LoginRequestDto;
import org.pm.authservice.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Auth Service Tests")
class AuthServiceTest {

    private final String TEST_EMAIL = "test@example.com";
    private final String TEST_PASSWORD = "password";
    private final String TEST_JWT = "test.jwt.token";
    @Mock
    private UserService userService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @InjectMocks
    private AuthService authService;
    private User testUser;
    private LoginRequestDto loginRequestDto;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .build();

        loginRequestDto = new LoginRequestDto(TEST_EMAIL, TEST_PASSWORD);
    }

    @Test
    @DisplayName("Should return token when credentials are valid")
    void authenticate_WithValidCredentials_ReturnsToken() {
        // Arrange
        when(userService.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(TEST_PASSWORD, TEST_PASSWORD)).thenReturn(true);
        when(jwtService.generateToken(testUser)).thenReturn(TEST_JWT);

        // Act
        Optional<String> result = authService.authenticate(loginRequestDto);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(TEST_JWT, result.get());
    }

    @Test
    @DisplayName("Should return empty when password is wrong")
    void authenticate_WithWrongPassword_ReturnsEmpty() {
        // Arrange
        when(userService.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("wrongPassword", TEST_PASSWORD)).thenReturn(false);

        loginRequestDto = new LoginRequestDto(TEST_EMAIL, "wrongPassword");

        // Act
        Optional<String> result = authService.authenticate(loginRequestDto);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should return empty when email does not exist")
    void authenticate_WithNonExistentEmail_ReturnsEmpty() {
        // Arrange
        when(userService.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        loginRequestDto = new LoginRequestDto("nonexistent@example.com", TEST_PASSWORD);

        // Act
        Optional<String> result = authService.authenticate(loginRequestDto);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should return empty when email is null")
    void authenticate_WithNullEmail_ReturnsEmpty() {
        // Arrange
        loginRequestDto = new LoginRequestDto(null, TEST_PASSWORD);

        // Act
        Optional<String> result = authService.authenticate(loginRequestDto);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should return empty when password is null")
    void authenticate_WithNullPassword_ReturnsEmpty() {
        // Arrange
        loginRequestDto = new LoginRequestDto(TEST_EMAIL, null);

        // Act
        Optional<String> result = authService.authenticate(loginRequestDto);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should return true when token is valid")
    void isTokenValid_WithValidToken_ReturnsTrue() {
        // Arrange
        when(jwtService.extractUsername(TEST_JWT)).thenReturn(TEST_EMAIL);
        when(userService.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(testUser));
        when(jwtService.isTokenValid(TEST_JWT, testUser)).thenReturn(true);

        // Act
        boolean result = authService.isTokenValid(TEST_JWT);

        // Assert
        assertTrue(result);
    }

    @Test
    @DisplayName("Should return false when user does not exist")
    void isTokenValid_WithNonExistentUser_ReturnsFalse() {
        // Arrange
        when(jwtService.extractUsername(TEST_JWT)).thenReturn(TEST_EMAIL);
        when(userService.findByEmail(TEST_EMAIL)).thenReturn(Optional.empty());

        // Act
        boolean result = authService.isTokenValid(TEST_JWT);

        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("Should return false when token is expired")
    void isTokenValid_WithExpiredToken_ReturnsFalse() {
        // Arrange
        when(jwtService.extractUsername(TEST_JWT)).thenReturn(TEST_EMAIL);
        when(userService.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(testUser));
        when(jwtService.isTokenValid(TEST_JWT, testUser)).thenReturn(false);

        // Act
        boolean result = authService.isTokenValid(TEST_JWT);

        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("Should return false when token is malformed")
    void isTokenValid_WithMalformedToken_ReturnsFalse() {
        // Arrange
        when(jwtService.extractUsername(TEST_JWT)).thenThrow(new JwtException("Invalid token"));

        // Act
        boolean result = authService.isTokenValid(TEST_JWT);

        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("Should return false when token is null")
    void isTokenValid_WithNullToken_ReturnsFalse() {
        // Act
        boolean result = authService.isTokenValid(null);

        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("Should return false when token is empty")
    void isTokenValid_WithEmptyToken_ReturnsFalse() {
        // Act
        boolean result = authService.isTokenValid("");

        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("Should return false when token subject is wrong")
    void isTokenValid_WithWrongSubject_ReturnsFalse() {
        // Arrange
        when(jwtService.extractUsername(TEST_JWT)).thenReturn("wrong@example.com");
        when(userService.findByEmail("wrong@example.com")).thenReturn(Optional.of(testUser));
        when(jwtService.isTokenValid(TEST_JWT, testUser)).thenReturn(false);

        // Act
        boolean result = authService.isTokenValid(TEST_JWT);

        // Assert
        assertFalse(result);
    }

}