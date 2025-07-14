package org.pm.authservice.model;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testUserCreation() {
        User user = User.builder()
                .id(UUID.randomUUID())
                .email("test@example.com")
                .password("password123")
                .role(Role.PATIENT)
                .build();

        assertNotNull(user.getId());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("password123", user.getPassword());
        assertEquals(Role.PATIENT, user.getRole());
    }

    @Test
    void testUserDetails() {
        User user = User.builder()
                .email("test@example.com")
                .password("password123")
                .role(Role.PATIENT)
                .build();

        assertEquals("test@example.com", user.getUsername());
        assertTrue(user.isAccountNonExpired());
        assertTrue(user.isAccountNonLocked());
        assertTrue(user.isCredentialsNonExpired());
        assertTrue(user.isEnabled());
        assertEquals(1, user.getAuthorities().size());
        assertEquals("PATIENT", user.getAuthorities().iterator().next().getAuthority());
    }

    @Test
    void testDifferentRoleAuthorities() {
        User doctor = User.builder().role(Role.DOCTOR).build();
        User patient = User.builder().role(Role.PATIENT).build();

        assertEquals("DOCTOR", doctor.getAuthorities().iterator().next().getAuthority());
        assertEquals("PATIENT", patient.getAuthorities().iterator().next().getAuthority());
    }

    @Test
    void testSetterMethods() {
        User user = new User();
        UUID id = UUID.randomUUID();

        user.setId(id);
        user.setEmail("new@example.com");
        user.setPassword("newpass");
        user.setRole(Role.DOCTOR);

        assertEquals(id, user.getId());
        assertEquals("new@example.com", user.getEmail());
        assertEquals("newpass", user.getPassword());
        assertEquals(Role.DOCTOR, user.getRole());
    }
}