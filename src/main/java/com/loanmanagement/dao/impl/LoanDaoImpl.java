package com.loanmanagement.dao.impl;

import com.loanmanagement.dao.LoanDao;
import com.loanmanagement.model.Loan;
import com.loanmanagement.util.DBConnection;

import java.sql.*;

public class LoanDaoImpl implements LoanDao {

    @Override
    public void addLoan(Loan loan) {

        String sql = """
                INSERT INTO loans
                (application_id, customer_id, loan_type_id,
                 principal_amount, interest_rate, tenure_months,
                 total_payable, outstanding_amount, start_date,
                 status, created_by)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, loan.getApplicationId());
            statement.setInt(2, loan.getCustomerId());
            statement.setInt(3, loan.getLoanTypeId());
            statement.setDouble(4, loan.getPrincipalAmount());
            statement.setDouble(5, loan.getInterestRate());
            statement.setInt(6, loan.getTenureMonths());
            statement.setDouble(7, loan.getTotalPayable());
            statement.setDouble(8, loan.getOutstandingAmount());

            if (loan.getStartDate() == null ||
                    loan.getStartDate().isBlank()) {
                statement.setDate(
                        9,
                        new java.sql.Date(System.currentTimeMillis())
                );
            } else {
                statement.setDate(
                        9,
                        Date.valueOf(loan.getStartDate())
                );
            }

            statement.setString(10, loan.getStatus());

            if (loan.getCreatedBy() == 0) {
                statement.setNull(11, Types.INTEGER);
            } else {
                statement.setInt(11, loan.getCreatedBy());
            }

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error adding loan", e);
        }
    }

    @Override
    public Loan getLoanById(int loanId) {

        String sql = """
                SELECT loan_id, application_id, customer_id,
                       loan_type_id, principal_amount, interest_rate,
                       tenure_months, total_payable, outstanding_amount,
                       start_date, status, created_by
                FROM loans
                WHERE loan_id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, loanId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapLoan(resultSet);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error fetching loan", e);
        }

        return null;
    }

    @Override
    public void updateLoan(Loan loan) {

        String sql = """
                UPDATE loans
                SET application_id = ?,
                    customer_id = ?,
                    loan_type_id = ?,
                    principal_amount = ?,
                    interest_rate = ?,
                    tenure_months = ?,
                    total_payable = ?,
                    outstanding_amount = ?,
                    start_date = ?,
                    status = ?,
                    created_by = ?
                WHERE loan_id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, loan.getApplicationId());
            statement.setInt(2, loan.getCustomerId());
            statement.setInt(3, loan.getLoanTypeId());
            statement.setDouble(4, loan.getPrincipalAmount());
            statement.setDouble(5, loan.getInterestRate());
            statement.setInt(6, loan.getTenureMonths());
            statement.setDouble(7, loan.getTotalPayable());
            statement.setDouble(8, loan.getOutstandingAmount());

            if (loan.getStartDate() == null ||
                    loan.getStartDate().isBlank()) {
                statement.setNull(9, Types.DATE);
            } else {
                statement.setDate(
                        9,
                        Date.valueOf(loan.getStartDate())
                );
            }

            statement.setString(10, loan.getStatus());

            if (loan.getCreatedBy() == 0) {
                statement.setNull(11, Types.INTEGER);
            } else {
                statement.setInt(11, loan.getCreatedBy());
            }

            statement.setInt(12, loan.getLoanId());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error updating loan", e);
        }
    }

    @Override
    public void deleteLoan(int loanId) {

        String sql = """
                DELETE FROM loans
                WHERE loan_id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, loanId);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting loan", e);
        }
    }

    private Loan mapLoan(ResultSet resultSet) throws SQLException {

        Loan loan = new Loan();

        loan.setLoanId(
                resultSet.getInt("loan_id")
        );

        loan.setApplicationId(
                resultSet.getInt("application_id")
        );

        loan.setCustomerId(
                resultSet.getInt("customer_id")
        );

        loan.setLoanTypeId(
                resultSet.getInt("loan_type_id")
        );

        loan.setPrincipalAmount(
                resultSet.getDouble("principal_amount")
        );

        loan.setInterestRate(
                resultSet.getDouble("interest_rate")
        );

        loan.setTenureMonths(
                resultSet.getInt("tenure_months")
        );

        loan.setTotalPayable(
                resultSet.getDouble("total_payable")
        );

        loan.setOutstandingAmount(
                resultSet.getDouble("outstanding_amount")
        );

        Date startDate = resultSet.getDate("start_date");

        if (startDate != null) {
            loan.setStartDate(startDate.toString());
        }

        loan.setStatus(
                resultSet.getString("status")
        );

        loan.setCreatedBy(
                resultSet.getInt("created_by")
        );

        return loan;
    }
}