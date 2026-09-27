package com.loanmanagement.dao.impl;

import com.loanmanagement.dao.CustomerDao;
import com.loanmanagement.model.Customer;
import com.loanmanagement.util.DBConnection;

import java.sql.*;

public class CustomerDaoImpl implements CustomerDao {

    private static final String SQL_INSERT_CUSTOMER =
            "INSERT INTO customers " +
                    "(user_id, full_name, email, phone, dob, address, monthly_income, " +
                    "pan_number, aadhaar_last4, employment_type, kyc_status, kyc_remarks, " +
                    "kyc_verified_by, kyc_verified_at, credit_score, existing_emi, status, " +
                    "account_number, ifsc_code, bank_name) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_SELECT_CUSTOMER_BY_ID =
            "SELECT * FROM customers WHERE customer_id = ?";

    private static final String SQL_SELECT_CUSTOMER_BY_USER_ID =
            "SELECT * FROM customers WHERE user_id = ?";

    private static final String SQL_UPDATE_CUSTOMER =
            "UPDATE customers SET " +
                    "full_name = ?, email = ?, phone = ?, dob = ?, address = ?, " +
                    "monthly_income = ?, pan_number = ?, aadhaar_last4 = ?, " +
                    "employment_type = ?, kyc_status = ?, kyc_remarks = ?, " +
                    "kyc_verified_by = ?, kyc_verified_at = ?, credit_score = ?, " +
                    "existing_emi = ?, status = ?, account_number = ?, " +
                    "ifsc_code = ?, bank_name = ? " +
                    "WHERE customer_id = ?";

    private static final String SQL_DELETE_CUSTOMER =
            "DELETE FROM customers WHERE customer_id = ?";

    private static final String SQL_VERIFY_KYC =
            "UPDATE customers " +
                    "SET kyc_status = 'VERIFIED', " +
                    "kyc_verified_by = ?, " +
                    "kyc_verified_at = CURRENT_TIMESTAMP " +
                    "WHERE customer_id = ?";

    @Override
    public void addCustomer(Customer customer) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             SQL_INSERT_CUSTOMER,
                             Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, customer.getUserId());
            statement.setString(2, customer.getFullName());
            statement.setString(3, customer.getEmail());
            statement.setString(4, customer.getPhone());

            if (customer.getDob() == null ||
                    customer.getDob().isBlank()) {

                statement.setNull(5, Types.DATE);

            } else {

                statement.setDate(
                        5,
                        Date.valueOf(customer.getDob())
                );
            }

            statement.setString(6, customer.getAddress());

            statement.setDouble(
                    7,
                    customer.getMonthlyIncome()
            );

            statement.setString(
                    8,
                    customer.getPanNumber()
            );

            statement.setString(
                    9,
                    customer.getAadhaarLast4()
            );

            statement.setString(
                    10,
                    customer.getEmploymentType()
            );

            statement.setString(
                    11,
                    customer.getKycStatus() == null ||
                            customer.getKycStatus().isBlank()
                            ? "PENDING"
                            : customer.getKycStatus()
            );

            statement.setString(
                    12,
                    customer.getKycRemarks()
            );

            if (customer.getKycVerifiedBy() == 0) {

                statement.setNull(
                        13,
                        Types.INTEGER
                );

            } else {

                statement.setInt(
                        13,
                        customer.getKycVerifiedBy()
                );
            }

            if (customer.getKycVerifiedAt() == null ||
                    customer.getKycVerifiedAt().isBlank()) {

                statement.setNull(
                        14,
                        Types.TIMESTAMP
                );

            } else {

                statement.setTimestamp(
                        14,
                        Timestamp.valueOf(
                                customer.getKycVerifiedAt()
                        )
                );
            }

            if (customer.getCreditScore() == 0) {

                statement.setNull(
                        15,
                        Types.INTEGER
                );

            } else {

                statement.setInt(
                        15,
                        customer.getCreditScore()
                );
            }

            statement.setDouble(
                    16,
                    customer.getExistingEmi()
            );

            statement.setString(
                    17,
                    customer.getStatus() == null ||
                            customer.getStatus().isBlank()
                            ? "ACTIVE"
                            : customer.getStatus()
            );

            statement.setString(
                    18,
                    customer.getAccountNumber()
            );

            statement.setString(
                    19,
                    customer.getIfscCode()
            );

            statement.setString(
                    20,
                    customer.getBankName()
            );

            statement.executeUpdate();

            try (ResultSet resultSet =
                         statement.getGeneratedKeys()) {

                if (resultSet.next()) {

                    customer.setCustomerId(
                            resultSet.getInt(1)
                    );
                }
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error adding customer",
                    e
            );
        }
    }

    @Override
    public Customer getCustomerById(int customerId) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             SQL_SELECT_CUSTOMER_BY_ID)) {

            statement.setInt(1, customerId);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapCustomer(resultSet);
                }

                return null;
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error while fetching customer",
                    e
            );
        }
    }

    @Override
    public Customer getCustomerByUserId(int userId) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             SQL_SELECT_CUSTOMER_BY_USER_ID)) {

            statement.setInt(1, userId);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapCustomer(resultSet);
                }

                return null;
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error while fetching customer by user ID",
                    e
            );
        }
    }

    @Override
    public void updateCustomer(Customer customer) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             SQL_UPDATE_CUSTOMER)) {

            statement.setString(
                    1,
                    customer.getFullName()
            );

            statement.setString(
                    2,
                    customer.getEmail()
            );

            statement.setString(
                    3,
                    customer.getPhone()
            );

            if (customer.getDob() == null ||
                    customer.getDob().isBlank()) {

                statement.setNull(
                        4,
                        Types.DATE
                );

            } else {

                statement.setDate(
                        4,
                        Date.valueOf(customer.getDob())
                );
            }

            statement.setString(
                    5,
                    customer.getAddress()
            );

            statement.setDouble(
                    6,
                    customer.getMonthlyIncome()
            );

            statement.setString(
                    7,
                    customer.getPanNumber()
            );

            statement.setString(
                    8,
                    customer.getAadhaarLast4()
            );

            statement.setString(
                    9,
                    customer.getEmploymentType()
            );

            statement.setString(
                    10,
                    customer.getKycStatus()
            );

            statement.setString(
                    11,
                    customer.getKycRemarks()
            );

            if (customer.getKycVerifiedBy() == 0) {

                statement.setNull(
                        12,
                        Types.INTEGER
                );

            } else {

                statement.setInt(
                        12,
                        customer.getKycVerifiedBy()
                );
            }

            if (customer.getKycVerifiedAt() == null ||
                    customer.getKycVerifiedAt().isBlank()) {

                statement.setNull(
                        13,
                        Types.TIMESTAMP
                );

            } else {

                statement.setTimestamp(
                        13,
                        Timestamp.valueOf(
                                customer.getKycVerifiedAt()
                        )
                );
            }

            if (customer.getCreditScore() == 0) {

                statement.setNull(
                        14,
                        Types.INTEGER
                );

            } else {

                statement.setInt(
                        14,
                        customer.getCreditScore()
                );
            }

            statement.setDouble(
                    15,
                    customer.getExistingEmi()
            );

            statement.setString(
                    16,
                    customer.getStatus()
            );

            statement.setString(
                    17,
                    customer.getAccountNumber()
            );

            statement.setString(
                    18,
                    customer.getIfscCode()
            );

            statement.setString(
                    19,
                    customer.getBankName()
            );

            statement.setInt(
                    20,
                    customer.getCustomerId()
            );

            statement.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error updating customer",
                    e
            );
        }
    }

    @Override
    public void deleteCustomer(int customerId) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             SQL_DELETE_CUSTOMER)) {

            statement.setInt(1, customerId);

            statement.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error deleting customer",
                    e
            );
        }
    }

    @Override
    public void verifyKyc(int customerId, int officerId) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             SQL_VERIFY_KYC)) {

            statement.setInt(1, officerId);
            statement.setInt(2, customerId);

            int rowsUpdated =
                    statement.executeUpdate();

            if (rowsUpdated == 0) {

                throw new RuntimeException(
                        "Customer not found"
                );
            }

            System.out.println(
                    "KYC verified successfully."
            );

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error while verifying KYC",
                    e
            );
        }
    }

    private Customer mapCustomer(ResultSet resultSet)
            throws SQLException {

        Customer customer = new Customer();

        customer.setCustomerId(
                resultSet.getInt("customer_id")
        );

        customer.setUserId(
                resultSet.getInt("user_id")
        );

        customer.setFullName(
                resultSet.getString("full_name")
        );

        customer.setEmail(
                resultSet.getString("email")
        );

        customer.setPhone(
                resultSet.getString("phone")
        );

        Date dob =
                resultSet.getDate("dob");

        if (dob != null) {

            customer.setDob(
                    dob.toString()
            );
        }

        customer.setAddress(
                resultSet.getString("address")
        );

        customer.setMonthlyIncome(
                resultSet.getDouble("monthly_income")
        );

        customer.setPanNumber(
                resultSet.getString("pan_number")
        );

        customer.setAadhaarLast4(
                resultSet.getString("aadhaar_last4")
        );

        customer.setEmploymentType(
                resultSet.getString("employment_type")
        );

        customer.setKycStatus(
                resultSet.getString("kyc_status")
        );

        customer.setKycRemarks(
                resultSet.getString("kyc_remarks")
        );

        customer.setKycVerifiedBy(
                resultSet.getInt("kyc_verified_by")
        );

        Timestamp verifiedAt =
                resultSet.getTimestamp(
                        "kyc_verified_at"
                );

        if (verifiedAt != null) {

            customer.setKycVerifiedAt(
                    verifiedAt.toString()
            );
        }

        customer.setCreditScore(
                resultSet.getInt("credit_score")
        );

        customer.setExistingEmi(
                resultSet.getDouble("existing_emi")
        );

        customer.setStatus(
                resultSet.getString("status")
        );

        customer.setAccountNumber(
                resultSet.getString("account_number")
        );

        customer.setIfscCode(
                resultSet.getString("ifsc_code")
        );

        customer.setBankName(
                resultSet.getString("bank_name")
        );

        return customer;
    }
}