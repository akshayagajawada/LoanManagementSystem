package com.loanmanagement.dao.impl;

import com.loanmanagement.dao.LoanApplicationDao;
import com.loanmanagement.model.LoanApplication;
import com.loanmanagement.util.DBConnection;

import java.sql.*;

public class LoanApplicationDaoImpl implements LoanApplicationDao {

    @Override
    public void addLoanApplication(LoanApplication application) {

        String sql = """
                INSERT INTO loan_applications
                (customer_id, loan_type_id, requested_amount, tenure_months,
                 purpose, status, remarks, reviewed_by, applied_at, reviewed_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, application.getCustomerId());
            statement.setInt(2, application.getLoanTypeId());
            statement.setDouble(3, application.getRequestedAmount());
            statement.setInt(4, application.getTenureMonths());
            statement.setString(5, application.getPurpose());
            statement.setString(6, application.getStatus());
            statement.setString(7, application.getRemarks());

            if (application.getReviewedBy() == 0) {
                statement.setNull(8, Types.INTEGER);
            } else {
                statement.setInt(8, application.getReviewedBy());
            }

            if (application.getAppliedAt() == null ||
                    application.getAppliedAt().isBlank()) {
                statement.setTimestamp(9, new Timestamp(System.currentTimeMillis()));
            } else {
                statement.setTimestamp(
                        9,
                        Timestamp.valueOf(application.getAppliedAt())
                );
            }

            if (application.getReviewedAt() == null ||
                    application.getReviewedAt().isBlank()) {
                statement.setNull(10, Types.TIMESTAMP);
            } else {
                statement.setTimestamp(
                        10,
                        Timestamp.valueOf(application.getReviewedAt())
                );
            }

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error adding loan application", e);
        }
    }

    @Override
    public LoanApplication getLoanApplicationById(int applicationId) {

        String sql = """
                SELECT application_id, customer_id, loan_type_id,
                       requested_amount, tenure_months, purpose,
                       status, remarks, reviewed_by, applied_at, reviewed_at
                FROM loan_applications
                WHERE application_id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, applicationId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapLoanApplication(resultSet);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error fetching loan application", e);
        }

        return null;
    }

    @Override
    public void updateLoanApplication(LoanApplication application) {

        String sql = """
                UPDATE loan_applications
                SET customer_id = ?,
                    loan_type_id = ?,
                    requested_amount = ?,
                    tenure_months = ?,
                    purpose = ?,
                    status = ?,
                    remarks = ?,
                    reviewed_by = ?,
                    applied_at = ?,
                    reviewed_at = ?
                WHERE application_id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, application.getCustomerId());
            statement.setInt(2, application.getLoanTypeId());
            statement.setDouble(3, application.getRequestedAmount());
            statement.setInt(4, application.getTenureMonths());
            statement.setString(5, application.getPurpose());
            statement.setString(6, application.getStatus());
            statement.setString(7, application.getRemarks());

            if (application.getReviewedBy() == 0) {
                statement.setNull(8, Types.INTEGER);
            } else {
                statement.setInt(8, application.getReviewedBy());
            }

            if (application.getAppliedAt() == null ||
                    application.getAppliedAt().isBlank()) {
                statement.setNull(9, Types.TIMESTAMP);
            } else {
                statement.setTimestamp(
                        9,
                        Timestamp.valueOf(application.getAppliedAt())
                );
            }

            if (application.getReviewedAt() == null ||
                    application.getReviewedAt().isBlank()) {
                statement.setNull(10, Types.TIMESTAMP);
            } else {
                statement.setTimestamp(
                        10,
                        Timestamp.valueOf(application.getReviewedAt())
                );
            }

            statement.setInt(11, application.getApplicationId());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error updating loan application", e);
        }
    }

    @Override
    public void deleteLoanApplication(int applicationId) {

        String sql = """
                DELETE FROM loan_applications
                WHERE application_id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, applicationId);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting loan application", e);
        }
    }

    private LoanApplication mapLoanApplication(ResultSet resultSet)
            throws SQLException {

        LoanApplication application = new LoanApplication();

        application.setApplicationId(
                resultSet.getInt("application_id")
        );

        application.setCustomerId(
                resultSet.getInt("customer_id")
        );

        application.setLoanTypeId(
                resultSet.getInt("loan_type_id")
        );

        application.setRequestedAmount(
                resultSet.getDouble("requested_amount")
        );

        application.setTenureMonths(
                resultSet.getInt("tenure_months")
        );

        application.setPurpose(
                resultSet.getString("purpose")
        );

        application.setStatus(
                resultSet.getString("status")
        );

        application.setRemarks(
                resultSet.getString("remarks")
        );

        application.setReviewedBy(
                resultSet.getInt("reviewed_by")
        );

        Timestamp appliedAt = resultSet.getTimestamp("applied_at");
        if (appliedAt != null) {
            application.setAppliedAt(appliedAt.toString());
        }

        Timestamp reviewedAt = resultSet.getTimestamp("reviewed_at");
        if (reviewedAt != null) {
            application.setReviewedAt(reviewedAt.toString());
        }

        return application;
    }
}