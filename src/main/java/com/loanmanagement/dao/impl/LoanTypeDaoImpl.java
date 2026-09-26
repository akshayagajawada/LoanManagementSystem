package com.loanmanagement.dao.impl;

import com.loanmanagement.dao.LoanTypeDao;
import com.loanmanagement.model.LoanType;
import com.loanmanagement.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LoanTypeDaoImpl implements LoanTypeDao {

    private static final String SQL_INSERT_LOAN_TYPE = """
            INSERT INTO loan_types
            (name, description, interest_rate, min_amount,
             max_amount, max_tenure_months, status)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;

    private static final String SQL_SELECT_LOAN_TYPE_BY_ID = """
            SELECT loan_type_id, name, description, interest_rate,
                   min_amount, max_amount, max_tenure_months, status
            FROM loan_types
            WHERE loan_type_id = ?
            """;

    private static final String SQL_UPDATE_LOAN_TYPE = """
            UPDATE loan_types
            SET name = ?,
                description = ?,
                interest_rate = ?,
                min_amount = ?,
                max_amount = ?,
                max_tenure_months = ?,
                status = ?
            WHERE loan_type_id = ?
            """;

    private static final String SQL_DELETE_LOAN_TYPE =
            "DELETE FROM loan_types WHERE loan_type_id = ?";

    @Override
    public void addLoanType(LoanType loanType) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             SQL_INSERT_LOAN_TYPE,
                             Statement.RETURN_GENERATED_KEYS
                     )) {

            statement.setString(1, loanType.getName());
            statement.setString(2, loanType.getDescription());
            statement.setDouble(3, loanType.getInterestRate());
            statement.setDouble(4, loanType.getMinAmount());
            statement.setDouble(5, loanType.getMaxAmount());
            statement.setInt(6, loanType.getMaxTenureMonths());
            statement.setString(
                    7,
                    loanType.getStatus() == null
                            ? "ACTIVE"
                            : loanType.getStatus()
            );

            statement.executeUpdate();

            try (ResultSet resultSet =
                         statement.getGeneratedKeys()) {

                if (resultSet.next()) {
                    loanType.setLoanTypeId(
                            resultSet.getInt(1)
                    );
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error while adding loan type", e
            );
        }
    }

    @Override
    public LoanType getLoanTypeById(int loanTypeId) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             SQL_SELECT_LOAN_TYPE_BY_ID)) {

            statement.setInt(1, loanTypeId);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapLoanType(resultSet);
                }

                return null;
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error while fetching loan type", e
            );
        }
    }

    @Override
    public void updateLoanType(LoanType loanType) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             SQL_UPDATE_LOAN_TYPE)) {

            statement.setString(1, loanType.getName());
            statement.setString(2, loanType.getDescription());
            statement.setDouble(3, loanType.getInterestRate());
            statement.setDouble(4, loanType.getMinAmount());
            statement.setDouble(5, loanType.getMaxAmount());
            statement.setInt(6, loanType.getMaxTenureMonths());
            statement.setString(7, loanType.getStatus());
            statement.setInt(8, loanType.getLoanTypeId());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error while updating loan type", e
            );
        }
    }

    @Override
    public void deleteLoanType(int loanTypeId) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             SQL_DELETE_LOAN_TYPE)) {

            statement.setInt(1, loanTypeId);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error while deleting loan type", e
            );
        }
    }

    private LoanType mapLoanType(
            ResultSet resultSet) throws SQLException {

        LoanType loanType = new LoanType();

        loanType.setLoanTypeId(
                resultSet.getInt("loan_type_id")
        );

        loanType.setName(
                resultSet.getString("name")
        );

        loanType.setDescription(
                resultSet.getString("description")
        );

        loanType.setInterestRate(
                resultSet.getDouble("interest_rate")
        );

        loanType.setMinAmount(
                resultSet.getDouble("min_amount")
        );

        loanType.setMaxAmount(
                resultSet.getDouble("max_amount")
        );

        loanType.setMaxTenureMonths(
                resultSet.getInt("max_tenure_months")
        );

        loanType.setStatus(
                resultSet.getString("status")
        );

        return loanType;
    }
}