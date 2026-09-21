package com.loanmanagement.dao;

import com.loanmanagement.model.User;

import java.util.List;

public interface UserDao {

        void addUser(User user);

        User getUserById(int userId);

        User getUserByUsername(String username);

        List<User> getAllUsers();

        void updateUser(User user);

        void deleteUser(int userId);
}