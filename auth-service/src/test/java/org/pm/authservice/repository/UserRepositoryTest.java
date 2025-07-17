package org.pm.authservice.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.pm.authservice.model.Role;
import org.pm.authservice.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    private User user;

    @BeforeEach
    void setup(){
        user = User.builder()
                .email("email@example.com")
                .password("password")
                .role(Role.ADMIN)
                .build();
        userRepository.save(user);
    }

    @Test
    @DisplayName("findByEmail should return user by email when exists")
    void findByEmail_shouldReturnUser_whenExists(){
        Optional<User> result = userRepository.findByEmail(user.getEmail());
        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    @DisplayName("findByEmail should return empty")
    void findByEmail_shouldReturnEmpty_whenNotExists() {
        Optional<User> result = userRepository.findByEmail("different@example.com");
        assertTrue(result.isEmpty());
        assertEquals(Optional.empty(), result);
    }

    @Test
    @DisplayName("findByEmail should return empty if email case doesn't match (PostgreSQL case-sensitive)")
    void findByEmail_shouldReturnEmpty_ifCaseDoesNotMatch() {
        Optional<User> result = userRepository.findByEmail("EMAIL@EXAMPLE.COM"); // Uppercase email
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("findByEmail should return empty when null is passed (PostgreSQL behavior)")
    void findByEmail_shouldReturnEmpty_whenNullIsPassed() {
        Optional<User> result = userRepository.findByEmail(null);
        assertTrue(result.isEmpty());
    }

}