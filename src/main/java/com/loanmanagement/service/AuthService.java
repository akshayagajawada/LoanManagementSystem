package com.loanmanagement.service;

import com.loanmanagement.model.User;

public interface AuthService {

    boolean login(String username, String password);

    User getUserByUsername(String username);

    void logout(int userId);
}