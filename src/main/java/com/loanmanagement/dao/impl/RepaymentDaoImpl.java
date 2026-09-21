package com.loanmanagement.dao.impl;

import com.loanmanagement.dao.RepaymentDao;
import com.loanmanagement.model.Repayment;
import com.loanmanagement.util.DBConnection;

import java.sql.*;

public class RepaymentDaoImpl implements RepaymentDao {

    @Override
    public void addRepayment(Repayment repayment) {

        String sql = """
                INSERT INTO repayments
                (loan_id, amount, payment_date, payment_mode,
                 reference_no, remarks, recorded_by, created_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, repayment.getLoanId());
            statement.setDouble(2, repayment.getAmount());

            if (repayment.getPaymentDate() == null ||
                    repayment.getPaymentDate().isBlank()) {
                statement.setDate(
                        3,
                        new java.sql.Date(System.currentTimeMillis())
                );
            } else {
                statement.setDate(
                        3,
                        Date.valueOf(repayment.getPaymentDate())
                );
            }

            statement.setString(4, repayment.getPaymentMode());
            statement.setString(5, repayment.getReferenceNo());
            statement.setString(6, repayment.getRemarks());

            if (repayment.getRecordedBy() == 0) {
                statement.setNull(7, Types.INTEGER);
            } else {
                statement.setInt(7, repayment.getRecordedBy());
            }

            if (repayment.getCreatedAt() == null ||
                    repayment.getCreatedAt().isBlank()) {
                statement.setTimestamp(
                        8,
                        new Timestamp(System.currentTimeMillis())
                );
            } else {
                statement.setTimestamp(
                        8,
                        Timestamp.valueOf(repayment.getCreatedAt())
                );
            }

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error adding repayment", e);
        }
    }

    @Override
    public Repayment getRepaymentById(int repaymentId) {

        String sql = """
                SELECT repayment_id, loan_id, amount,
                       payment_date, payment_mode, reference_no,
                       remarks, recorded_by, created_at
                FROM repayments
                WHERE repayment_id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, repaymentId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapRepayment(resultSet);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error fetching repayment", e);
        }

        return null;
    }

    @Override
    public void updateRepayment(Repayment repayment) {

        String sql = """
                UPDATE repayments
                SET loan_id = ?,
                    amount = ?,
                    payment_date = ?,
                    payment_mode = ?,
                    reference_no = ?,
                    remarks = ?,
                    recorded_by = ?,
                    created_at = ?
                WHERE repayment_id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, repayment.getLoanId());
            statement.setDouble(2, repayment.getAmount());

            if (repayment.getPaymentDate() == null ||
                    repayment.getPaymentDate().isBlank()) {
                statement.setNull(3, Types.DATE);
            } else {
                statement.setDate(
                        3,
                        Date.valueOf(repayment.getPaymentDate())
                );
            }

            statement.setString(4, repayment.getPaymentMode());
            statement.setString(5, repayment.getReferenceNo());
            statement.setString(6, repayment.getRemarks());

            if (repayment.getRecordedBy() == 0) {
                statement.setNull(7, Types.INTEGER);
            } else {
                statement.setInt(7, repayment.getRecordedBy());
            }

            if (repayment.getCreatedAt() == null ||
                    repayment.getCreatedAt().isBlank()) {
                statement.setNull(8, Types.TIMESTAMP);
            } else {
                statement.setTimestamp(
                        8,
                        Timestamp.valueOf(repayment.getCreatedAt())
                );
            }

            statement.setInt(9, repayment.getRepaymentId());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error updating repayment", e);
        }
    }

    @Override
    public void deleteRepayment(int repaymentId) {

        String sql = """
                DELETE FROM repayments
                WHERE repayment_id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, repaymentId);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting repayment", e);
        }
    }

    private Repayment mapRepayment(ResultSet resultSet)
            throws SQLException {

        Repayment repayment = new Repayment();

        repayment.setRepaymentId(
                resultSet.getInt("repayment_id")
        );

        repayment.setLoanId(
                resultSet.getInt("loan_id")
        );

        repayment.setAmount(
                resultSet.getDouble("amount")
        );

        Date paymentDate = resultSet.getDate("payment_date");

        if (paymentDate != null) {
            repayment.setPaymentDate(paymentDate.toString());
        }

        repayment.setPaymentMode(
                resultSet.getString("payment_mode")
        );

        repayment.setReferenceNo(
                resultSet.getString("reference_no")
        );

        repayment.setRemarks(
                resultSet.getString("remarks")
        );

        repayment.setRecordedBy(
                resultSet.getInt("recorded_by")
        );

        Timestamp createdAt = resultSet.getTimestamp("created_at");

        if (createdAt != null) {
            repayment.setCreatedAt(createdAt.toString());
        }

        return repayment;
    }
}