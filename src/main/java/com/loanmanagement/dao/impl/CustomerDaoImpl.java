package com.loanmanagement.dao.impl;

import com.loanmanagement.dao.CustomerDao;
import com.loanmanagement.model.Customer;
import com.loanmanagement.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerDaoImpl implements CustomerDao {

    private static final String SQL_INSERT_CUSTOMER = """
            INSERT INTO customers
            (user_id, full_name, email, phone, dob, address, monthly_income,
             pan_number, aadhaar_last4, employment_type, kyc_status,
             kyc_remarks, kyc_verified_by, kyc_verified_at,
             credit_score, existing_emi, status)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

    private static final String SQL_SELECT_CUSTOMER_BY_ID = """
            SELECT customer_id, user_id, full_name, email, phone, dob,
                   address, monthly_income, pan_number, aadhaar_last4,
                   employment_type, account_number, ifsc_code, bank_name,
                   kyc_status, kyc_remarks, kyc_verified_by,
                   kyc_verified_at, credit_score, existing_emi, status
            FROM customers
            WHERE customer_id = ?
            """;

    private static final String SQL_UPDATE_CUSTOMER = """
            UPDATE customers
            SET user_id = ?,
                full_name = ?,
                email = ?,
                phone = ?,
                dob = ?,
                address = ?,
                monthly_income = ?,
                pan_number = ?,
                aadhaar_last4 = ?,
                employment_type = ?,
                account_number = ?,
                ifsc_code = ?,
                bank_name = ?,
                kyc_status = ?,
                kyc_remarks = ?,
                kyc_verified_by = ?,
                kyc_verified_at = ?,
                credit_score = ?,
                existing_emi = ?,
                status = ?
            WHERE customer_id = ?
            """;

    private static final String SQL_DELETE_CUSTOMER =
            "DELETE FROM customers WHERE customer_id = ?";

    @Override
    public void addCustomer(Customer customer) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(SQL_INSERT_CUSTOMER)) {

            statement.setInt(1, customer.getUserId());
            statement.setString(2, customer.getFullName());
            statement.setString(3, customer.getEmail());
            statement.setString(4, customer.getPhone());

            if (customer.getDob() != null && !customer.getDob().isBlank()) {
                statement.setDate(5, java.sql.Date.valueOf(customer.getDob()));
            } else {
                statement.setNull(5, java.sql.Types.DATE);
            }

            statement.setString(6, customer.getAddress());
            statement.setDouble(7, customer.getMonthlyIncome());
            statement.setString(8, customer.getPanNumber());
            statement.setString(9, customer.getAadhaarLast4());
            statement.setString(10, customer.getEmploymentType());

            statement.setString(11,
                    customer.getKycStatus() == null
                            ? "PENDING"
                            : customer.getKycStatus());

            statement.setString(12, customer.getKycRemarks());

            if (customer.getKycVerifiedBy() > 0) {
                statement.setInt(13, customer.getKycVerifiedBy());
            } else {
                statement.setNull(13, java.sql.Types.INTEGER);
            }

            if (customer.getKycVerifiedAt() != null
                    && !customer.getKycVerifiedAt().isBlank()) {
                statement.setTimestamp(
                        14,
                        java.sql.Timestamp.valueOf(customer.getKycVerifiedAt())
                );
            } else {
                statement.setNull(14, java.sql.Types.TIMESTAMP);
            }

            if (customer.getCreditScore() > 0) {
                statement.setInt(15, customer.getCreditScore());
            } else {
                statement.setNull(15, java.sql.Types.INTEGER);
            }

            statement.setDouble(16, customer.getExistingEmi());

            statement.setString(17,
                    customer.getStatus() == null
                            ? "ACTIVE"
                            : customer.getStatus());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error while adding customer", e);
        }
    }

    @Override
    public Customer getCustomerById(int customerId) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(SQL_SELECT_CUSTOMER_BY_ID)) {

            statement.setInt(1, customerId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapCustomer(resultSet);
                }

                return null;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error while fetching customer", e);
        }
    }

    @Override
    public void updateCustomer(Customer customer) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(SQL_UPDATE_CUSTOMER)) {

            statement.setInt(1, customer.getUserId());
            statement.setString(2, customer.getFullName());
            statement.setString(3, customer.getEmail());
            statement.setString(4, customer.getPhone());

            if (customer.getDob() != null && !customer.getDob().isBlank()) {
                statement.setDate(5, java.sql.Date.valueOf(customer.getDob()));
            } else {
                statement.setNull(5, java.sql.Types.DATE);
            }

            statement.setString(6, customer.getAddress());
            statement.setDouble(7, customer.getMonthlyIncome());
            statement.setString(8, customer.getPanNumber());
            statement.setString(9, customer.getAadhaarLast4());
            statement.setString(10, customer.getEmploymentType());
            statement.setString(11, customer.getAccountNumber());
            statement.setString(12, customer.getIfscCode());
            statement.setString(13, customer.getBankName());
            statement.setString(14, customer.getKycStatus());
            statement.setString(15, customer.getKycRemarks());

            if (customer.getKycVerifiedBy() > 0) {
                statement.setInt(16, customer.getKycVerifiedBy());
            } else {
                statement.setNull(16, java.sql.Types.INTEGER);
            }

            if (customer.getKycVerifiedAt() != null
                    && !customer.getKycVerifiedAt().isBlank()) {
                statement.setTimestamp(
                        17,
                        java.sql.Timestamp.valueOf(customer.getKycVerifiedAt())
                );
            } else {
                statement.setNull(17, java.sql.Types.TIMESTAMP);
            }

            if (customer.getCreditScore() > 0) {
                statement.setInt(18, customer.getCreditScore());
            } else {
                statement.setNull(18, java.sql.Types.INTEGER);
            }

            statement.setDouble(19, customer.getExistingEmi());
            statement.setString(20, customer.getStatus());
            statement.setInt(21, customer.getCustomerId());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error while updating customer", e);
        }
    }

    @Override
    public void deleteCustomer(int customerId) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(SQL_DELETE_CUSTOMER)) {

            statement.setInt(1, customerId);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error while deleting customer", e);
        }
    }

    private Customer mapCustomer(ResultSet resultSet) throws SQLException {

        Customer customer = new Customer();

        customer.setCustomerId(resultSet.getInt("customer_id"));
        customer.setUserId(resultSet.getInt("user_id"));
        customer.setFullName(resultSet.getString("full_name"));
        customer.setEmail(resultSet.getString("email"));
        customer.setPhone(resultSet.getString("phone"));

        java.sql.Date dob = resultSet.getDate("dob");

        if (dob != null) {
            customer.setDob(dob.toString());
        }

        customer.setAddress(resultSet.getString("address"));
        customer.setMonthlyIncome(resultSet.getDouble("monthly_income"));
        customer.setPanNumber(resultSet.getString("pan_number"));
        customer.setAadhaarLast4(resultSet.getString("aadhaar_last4"));
        customer.setEmploymentType(resultSet.getString("employment_type"));
        customer.setAccountNumber(resultSet.getString("account_number"));
        customer.setIfscCode(resultSet.getString("ifsc_code"));
        customer.setBankName(resultSet.getString("bank_name"));
        customer.setKycStatus(resultSet.getString("kyc_status"));
        customer.setKycRemarks(resultSet.getString("kyc_remarks"));
        customer.setKycVerifiedBy(resultSet.getInt("kyc_verified_by"));

        java.sql.Timestamp verifiedAt =
                resultSet.getTimestamp("kyc_verified_at");

        if (verifiedAt != null) {
            customer.setKycVerifiedAt(verifiedAt.toString());
        }

        customer.setCreditScore(resultSet.getInt("credit_score"));
        customer.setExistingEmi(resultSet.getDouble("existing_emi"));
        customer.setStatus(resultSet.getString("status"));

        return customer;
    }
}