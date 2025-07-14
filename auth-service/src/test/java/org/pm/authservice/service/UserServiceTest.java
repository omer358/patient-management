package org.pm.authservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pm.authservice.model.User;
import org.pm.authservice.repository.UserRepository;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository);
    }

    @Test
    void findByEmail_WhenUserExists_ReturnsUser() {
        // Arrange
        String email = "test@example.com";
        User expectedUser = User.builder()
                .id(UUID.randomUUID())
                .email(email)
                .build();
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(expectedUser));

        // Act
        Optional<User> result = userService.findByEmail(email);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(expectedUser, result.get());
        verify(userRepository).findByEmail(email);
    }

    @Test
    void findByEmail_WhenUserDoesNotExist_ReturnsEmptyOptional() {
        // Arrange
        String email = "nonexistent@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act
        Optional<User> result = userService.findByEmail(email);

        // Assert
        assertTrue(result.isEmpty());
        verify(userRepository).findByEmail(email);
    }

    @Test
    void findByEmail_WhenEmailIsNull_ReturnsEmptyOptional() {
        // Arrange
        when(userRepository.findByEmail(null)).thenReturn(Optional.empty());

        // Act
        Optional<User> result = userService.findByEmail(null);

        // Assert
        assertTrue(result.isEmpty());
        verify(userRepository).findByEmail(null);
    }
}