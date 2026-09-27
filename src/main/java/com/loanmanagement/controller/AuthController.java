package com.loanmanagement.controller;

import com.loanmanagement.model.User;
import com.loanmanagement.service.AuthService;
import com.loanmanagement.service.impl.AuthServiceImpl;

public class AuthController {

    private final AuthService authService;

    public AuthController() {
        this.authService = new AuthServiceImpl();
    }

    public boolean login(String username, String password) {
        return authService.login(username, password);
    }

    public User getUserByUsername(String username) {
        return authService.getUserByUsername(username);
    }

    public void logout(int userId) {
        authService.logout(userId);
    }
}