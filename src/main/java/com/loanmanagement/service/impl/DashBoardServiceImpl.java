package com.loanmanagement.service.impl;

import com.loanmanagement.service.DashBoardService;
import com.loanmanagement.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DashBoardServiceImpl implements DashBoardService {

    @Override
    public int getTotalCustomers() {

        String sql = "SELECT COUNT(*) FROM customers";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error getting total customers", e
            );
        }

        return 0;
    }

    @Override
    public int getTotalLoans() {

        String sql = "SELECT COUNT(*) FROM loans";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error getting total loans", e
            );
        }

        return 0;
    }

    @Override
    public double getTotalLoanAmount() {

        String sql = "SELECT COALESCE(SUM(principal_amount), 0) FROM loans";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getDouble(1);
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error getting total loan amount", e
            );
        }

        return 0;
    }

    @Override
    public double getTotalRepaymentAmount() {

        String sql = "SELECT COALESCE(SUM(amount), 0) FROM repayments";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getDouble(1);
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error getting total repayment amount", e
            );
        }

        return 0;
    }
}