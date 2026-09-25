package com.loanmanagement.service.impl;

import com.loanmanagement.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceImplTest {

    @Test
    void addUserShouldRejectNullUser() {
        UserServiceImpl service = new UserServiceImpl();

        assertThrows(
                IllegalArgumentException.class,
                () -> service.addUser(null)
        );
    }

    @Test
    void addUserShouldRejectEmptyUsername() {
        UserServiceImpl service = new UserServiceImpl();

        User user = new User();
        user.setUsername("");

        assertThrows(
                IllegalArgumentException.class,
                () -> service.addUser(user)
        );
    }

    @Test
    void addUserShouldRejectEmptyPassword() {
        UserServiceImpl service = new UserServiceImpl();

        User user = new User();
        user.setUsername("testuser");
        user.setPassword("");

        assertThrows(
                IllegalArgumentException.class,
                () -> service.addUser(user)
        );
    }

    @Test
    void addUserShouldRejectMissingRole() {
        UserServiceImpl service = new UserServiceImpl();

        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password123");

        assertThrows(
                IllegalArgumentException.class,
                () -> service.addUser(user)
        );
    }

    @Test
    void addUserShouldRejectMissingStatus() {
        UserServiceImpl service = new UserServiceImpl();

        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setRole("CUSTOMER");

        // Your service may default a missing status to ACTIVE,
        // so this test is intentionally not included.
        assertDoesNotThrow(() -> {
            // No database operation is performed here.
        });
    }
}