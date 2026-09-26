package com.loanmanagement.dao.impl;

import com.loanmanagement.dao.UserDao;
import com.loanmanagement.model.User;
import com.loanmanagement.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao {

    private static final String SQL_INSERT_USER = """
            INSERT INTO users
            (username, password_hash, role, status)
            VALUES (?, ?, ?, ?)
            """;

    private static final String SQL_SELECT_USER_BY_ID =
            "SELECT * FROM users WHERE user_id = ?";

    private static final String SQL_SELECT_USER_BY_USERNAME =
            "SELECT * FROM users WHERE username = ?";

    private static final String SQL_SELECT_ALL_USERS =
            "SELECT * FROM users ORDER BY user_id";

    private static final String SQL_UPDATE_USER = """
            UPDATE users
            SET username = ?,
                password_hash = ?,
                role = ?,
                status = ?
            WHERE user_id = ?
            """;

    private static final String SQL_DELETE_USER =
            "DELETE FROM users WHERE user_id = ?";

    @Override
    public void addUser(User user) {

        try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(SQL_INSERT_USER)
        ) {

            statement.setString(
                    1,
                    user.getUsername()
            );

            statement.setString(
                    2,
                    user.getPassword()
            );

            statement.setString(
                    3,
                    user.getRole()
            );

            statement.setString(
                    4,
                    user.getStatus()
            );

            statement.executeUpdate();

        } catch (Exception e) {

            throw new RuntimeException(
                    "Error adding user",
                    e
            );
        }
    }

    @Override
    public User getUserById(int userId) {

        try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(
                                SQL_SELECT_USER_BY_ID
                        )
        ) {

            statement.setInt(1, userId);

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                if (resultSet.next()) {
                    return mapUser(resultSet);
                }
            }

        } catch (Exception e) {

            throw new RuntimeException(
                    "Error getting user",
                    e
            );
        }

        return null;
    }

    @Override
    public User getUserByUsername(String username) {

        try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(
                                SQL_SELECT_USER_BY_USERNAME
                        )
        ) {

            statement.setString(
                    1,
                    username
            );

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                if (resultSet.next()) {
                    return mapUser(resultSet);
                }
            }

        } catch (Exception e) {

            throw new RuntimeException(
                    "Error getting user by username",
                    e
            );
        }

        return null;
    }

    @Override
    public List<User> getAllUsers() {

        List<User> users =
                new ArrayList<>();

        try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(
                                SQL_SELECT_ALL_USERS
                        );

                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            while (resultSet.next()) {

                users.add(
                        mapUser(resultSet)
                );
            }

        } catch (Exception e) {

            throw new RuntimeException(
                    "Error getting all users",
                    e
            );
        }

        return users;
    }

    @Override
    public void updateUser(User user) {

        try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(
                                SQL_UPDATE_USER
                        )
        ) {

            statement.setString(
                    1,
                    user.getUsername()
            );

            statement.setString(
                    2,
                    user.getPassword()
            );

            statement.setString(
                    3,
                    user.getRole()
            );

            statement.setString(
                    4,
                    user.getStatus()
            );

            statement.setInt(
                    5,
                    user.getUserId()
            );

            statement.executeUpdate();

        } catch (Exception e) {

            throw new RuntimeException(
                    "Error updating user",
                    e
            );
        }
    }

    @Override
    public void deleteUser(int userId) {

        try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(
                                SQL_DELETE_USER
                        )
        ) {

            statement.setInt(1, userId);

            statement.executeUpdate();

        } catch (Exception e) {

            throw new RuntimeException(
                    "Error deleting user",
                    e
            );
        }
    }

    // =========================
    // MAP RESULT TO USER
    // =========================

    private User mapUser(ResultSet resultSet)
            throws Exception {

        User user = new User();

        user.setUserId(
                resultSet.getInt("user_id")
        );

        user.setUsername(
                resultSet.getString("username")
        );

        user.setPassword(
                resultSet.getString("password_hash")
        );

        user.setRole(
                resultSet.getString("role")
        );

        user.setStatus(
                resultSet.getString("status")
        );

        if (resultSet.getTimestamp(
                "created_at") != null) {

            user.setCreatedAt(
                    resultSet
                            .getTimestamp("created_at")
                            .toString()
            );
        }

        return user;
    }
}