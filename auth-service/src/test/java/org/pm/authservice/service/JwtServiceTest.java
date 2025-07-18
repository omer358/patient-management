package org.pm.authservice.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pm.authservice.model.Role;
import org.pm.authservice.model.User;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private static final String SECRET_KEY = "BZiIguPNb9xsyuSsuYiFFk+zBp8HCQlP3mZy3XSk3E4=";
    private static final long EXPIRATION_MILLIS = 3600000; // 1 hour
    private static final Instant FIXED_TIME = java.time.ZonedDateTime
            .now()
            .plusYears(5)
            .withHour(12).withMinute(0).withSecond(0).withNano(0)
            .toInstant();
    private JwtService jwtService;
    private Clock fixedClock;

    @BeforeEach
    void setUp() {
        fixedClock = Clock.fixed(FIXED_TIME, ZoneId.systemDefault());
        jwtService = new JwtService(fixedClock);
        ReflectionTestUtils.setField(jwtService, "secretKey", SECRET_KEY);
        ReflectionTestUtils.setField(jwtService, "expirationMillis", EXPIRATION_MILLIS);
    }

    @Test
    void generateToken_ShouldCreateValidToken() {
        // Arrange
        User user = createTestUser();

        // Act
        String token = jwtService.generateToken(user);

        // Assert
        assertNotNull(token);
        assertTrue(jwtService.isTokenValid(token, user));
        assertEquals(user.getEmail(), jwtService.extractUsername(token));
    }

    @Test
    void isTokenValid_WithExpiredToken_ShouldReturnFalse() {
        // Arrange
        User user = createTestUser();

        // Use a normal clock for token generation
        Clock issueClock = Clock.fixed(FIXED_TIME, ZoneId.systemDefault());
        JwtService issuingJwtService = new JwtService(issueClock);
        ReflectionTestUtils.setField(issuingJwtService, "secretKey", SECRET_KEY);
        ReflectionTestUtils.setField(issuingJwtService, "expirationMillis", EXPIRATION_MILLIS);

        String expiredToken = issuingJwtService.generateToken(user);

        // Now simulate a time *after* token expiration
        Clock validationClock = Clock.fixed(FIXED_TIME.plusMillis(EXPIRATION_MILLIS + 1000), ZoneId.systemDefault());
        JwtService validatingJwtService = new JwtService(validationClock);
        ReflectionTestUtils.setField(validatingJwtService, "secretKey", SECRET_KEY);
        ReflectionTestUtils.setField(validatingJwtService, "expirationMillis", EXPIRATION_MILLIS);

        // Act & Assert
        assertFalse(validatingJwtService.isTokenValid(expiredToken, user));
    }


    @Test
    void extractClaim_ShouldReturnCorrectClaims() {
        // Arrange
        User user = createTestUser();
        String token = jwtService.generateToken(user);

        // Act & Assert
        assertEquals(user.getEmail(), jwtService.extractClaim(token, Claims::getSubject));
        assertEquals(Role.PATIENT.name(), jwtService.extractClaim(token, claims -> claims.get("role", String.class)));
    }

    @Test
    void extractAllClaims_WithInvalidToken_ShouldThrowJwtException() {
        // Arrange
        String invalidToken = "invalid.token.here";

        // Act & Assert
        assertThrows(JwtException.class, () -> jwtService.extractUsername(invalidToken));
    }

    @Test
    void extractExpiration_ShouldReturnCorrectExpirationDate() {
        // Arrange
        User user = createTestUser();
        String token = jwtService.generateToken(user);

        // Act
        Date expirationDate = jwtService.extractExpiration(token);

        // Truncate milliseconds for predictable assertion
        Date expectedDate = Date.from(FIXED_TIME.plusMillis(EXPIRATION_MILLIS).truncatedTo(ChronoUnit.SECONDS));

        // Assert
        assertEquals(expectedDate, expirationDate);
    }

    private User createTestUser() {
        return User.builder()
                .id(UUID.randomUUID())
                .email("test@example.com")
                .password("password")
                .role(Role.PATIENT)
                .build();
    }
}