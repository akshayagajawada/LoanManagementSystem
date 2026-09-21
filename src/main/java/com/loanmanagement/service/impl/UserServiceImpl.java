package com.loanmanagement.service.impl;

import com.loanmanagement.dao.UserDao;
import com.loanmanagement.dao.impl.UserDaoImpl;
import com.loanmanagement.model.User;
import com.loanmanagement.service.UserService;
import com.loanmanagement.util.PasswordUtil;

import java.util.List;

public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    public UserServiceImpl() {
        this.userDao = new UserDaoImpl();
    }

    @Override
    public void addUser(User user) {

        if (user == null) {
            throw new IllegalArgumentException(
                    "User cannot be null"
            );
        }

        if (user.getUsername() == null
                || user.getUsername().isBlank()) {

            throw new IllegalArgumentException(
                    "Username is required"
            );
        }

        if (user.getPassword() == null
                || user.getPassword().isBlank()) {

            throw new IllegalArgumentException(
                    "Password is required"
            );
        }

        if (user.getRole() == null
                || user.getRole().isBlank()) {

            throw new IllegalArgumentException(
                    "Role is required"
            );
        }

        if (user.getStatus() == null
                || user.getStatus().isBlank()) {

            user.setStatus("ACTIVE");
        }

        // Hash password before saving
        user.setPassword(
                PasswordUtil.hashPassword(
                        user.getPassword()
                )
        );

        userDao.addUser(user);
    }

    @Override
    public User getUserById(int userId) {

        if (userId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid user ID"
            );
        }

        return userDao.getUserById(userId);
    }

    @Override
    public List<User> getAllUsers() {

        return userDao.getAllUsers();
    }

    @Override
    public void updateUser(User user) {

        if (user == null) {
            throw new IllegalArgumentException(
                    "User cannot be null"
            );
        }

        if (user.getUserId() <= 0) {
            throw new IllegalArgumentException(
                    "Invalid user ID"
            );
        }

        if (user.getUsername() == null
                || user.getUsername().isBlank()) {

            throw new IllegalArgumentException(
                    "Username is required"
            );
        }

        if (user.getRole() == null
                || user.getRole().isBlank()) {

            throw new IllegalArgumentException(
                    "Role is required"
            );
        }

        if (user.getStatus() == null
                || user.getStatus().isBlank()) {

            throw new IllegalArgumentException(
                    "Status is required"
            );
        }

        userDao.updateUser(user);
    }

    @Override
    public void deleteUser(int userId) {

        if (userId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid user ID"
            );
        }

        userDao.deleteUser(userId);
    }
}