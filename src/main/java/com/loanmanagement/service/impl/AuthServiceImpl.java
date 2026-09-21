package com.loanmanagement.service.impl;

import com.loanmanagement.dao.UserDao;
import com.loanmanagement.dao.impl.UserDaoImpl;
import com.loanmanagement.model.User;
import com.loanmanagement.service.AuthService;
import com.loanmanagement.util.PasswordUtil;

public class AuthServiceImpl implements AuthService {

    private final UserDao userDao;

    public AuthServiceImpl() {
        this.userDao = new UserDaoImpl();
    }

    @Override
    public boolean login(String username, String password) {

        User user = userDao.getUserByUsername(username);

        if (user == null) {
            return false;
        }

        if (!"ACTIVE".equalsIgnoreCase(user.getStatus())) {
            return false;
        }

        return PasswordUtil.verifyPassword(
                password,
                user.getPassword()
        );
    }

    @Override
    public User getUserByUsername(String username) {
        return userDao.getUserByUsername(username);
    }

    @Override
    public void logout(int userId) {
        // Logout/session handling will be implemented
        // when we build the controller and UI.
    }
}