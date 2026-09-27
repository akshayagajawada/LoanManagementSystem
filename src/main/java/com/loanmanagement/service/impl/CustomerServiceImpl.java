package com.loanmanagement.service.impl;

import com.loanmanagement.dao.CustomerDao;
import com.loanmanagement.dao.impl.CustomerDaoImpl;
import com.loanmanagement.model.Customer;
import com.loanmanagement.service.CustomerService;

public class CustomerServiceImpl implements CustomerService {

    private final CustomerDao customerDao;

    public CustomerServiceImpl() {
        this.customerDao = new CustomerDaoImpl();
    }

    @Override
    public void addCustomer(Customer customer) {

        if (customer == null) {
            throw new IllegalArgumentException(
                    "Customer cannot be null"
            );
        }

        if (customer.getUserId() <= 0) {
            throw new IllegalArgumentException(
                    "Invalid user ID"
            );
        }

        if (customer.getFullName() == null ||
                customer.getFullName().isBlank()) {
            throw new IllegalArgumentException(
                    "Customer name is required"
            );
        }

        if (customer.getEmail() == null ||
                customer.getEmail().isBlank()) {
            throw new IllegalArgumentException(
                    "Customer email is required"
            );
        }

        if (customer.getPhone() == null ||
                customer.getPhone().isBlank()) {
            throw new IllegalArgumentException(
                    "Customer phone is required"
            );
        }

        if (customer.getPanNumber() == null ||
                customer.getPanNumber().isBlank()) {
            throw new IllegalArgumentException(
                    "PAN number is required"
            );
        }

        if (customer.getMonthlyIncome() < 0) {
            throw new IllegalArgumentException(
                    "Monthly income cannot be negative"
            );
        }

        if (customer.getKycStatus() == null ||
                customer.getKycStatus().isBlank()) {
            customer.setKycStatus("PENDING");
        }

        if (customer.getStatus() == null ||
                customer.getStatus().isBlank()) {
            customer.setStatus("ACTIVE");
        }

        customerDao.addCustomer(customer);
    }

    @Override
    public Customer getCustomerById(int customerId) {

        if (customerId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid customer ID"
            );
        }

        return customerDao.getCustomerById(customerId);
    }

    @Override
    public Customer getCustomerByUserId(int userId) {

        if (userId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid user ID"
            );
        }

        return customerDao.getCustomerByUserId(userId);
    }

    @Override
    public void updateCustomer(Customer customer) {

        if (customer == null) {
            throw new IllegalArgumentException(
                    "Customer cannot be null"
            );
        }

        if (customer.getCustomerId() <= 0) {
            throw new IllegalArgumentException(
                    "Invalid customer ID"
            );
        }

        if (customer.getFullName() == null ||
                customer.getFullName().isBlank()) {
            throw new IllegalArgumentException(
                    "Customer name is required"
            );
        }

        if (customer.getEmail() == null ||
                customer.getEmail().isBlank()) {
            throw new IllegalArgumentException(
                    "Customer email is required"
            );
        }

        if (customer.getPhone() == null ||
                customer.getPhone().isBlank()) {
            throw new IllegalArgumentException(
                    "Customer phone is required"
            );
        }

        if (customer.getMonthlyIncome() < 0) {
            throw new IllegalArgumentException(
                    "Monthly income cannot be negative"
            );
        }

        customerDao.updateCustomer(customer);
    }

    @Override
    public void deleteCustomer(int customerId) {

        if (customerId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid customer ID"
            );
        }

        customerDao.deleteCustomer(customerId);
    }

    @Override
    public void verifyKyc(int customerId, int officerId) {

        if (customerId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid customer ID"
            );
        }

        if (officerId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid officer ID"
            );
        }

        Customer customer =
                customerDao.getCustomerById(customerId);

        if (customer == null) {
            throw new IllegalArgumentException(
                    "Customer not found"
            );
        }

        if ("VERIFIED".equalsIgnoreCase(
                customer.getKycStatus())) {

            throw new IllegalArgumentException(
                    "KYC is already verified"
            );
        }

        customerDao.verifyKyc(
                customerId,
                officerId
        );
    }
}