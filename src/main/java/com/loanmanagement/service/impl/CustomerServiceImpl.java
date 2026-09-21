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
            throw new IllegalArgumentException("Customer cannot be null");
        }

        if (customer.getFullName() == null ||
                customer.getFullName().isBlank()) {
            throw new IllegalArgumentException(
                    "Customer name cannot be empty"
            );
        }

        if (customer.getEmail() == null ||
                customer.getEmail().isBlank()) {
            throw new IllegalArgumentException(
                    "Email cannot be empty"
            );
        }

        if (customer.getPhone() == null ||
                customer.getPhone().isBlank()) {
            throw new IllegalArgumentException(
                    "Phone number cannot be empty"
            );
        }

        if (customer.getPanNumber() == null ||
                customer.getPanNumber().isBlank()) {
            throw new IllegalArgumentException(
                    "PAN number cannot be empty"
            );
        }

        if (customer.getMonthlyIncome() < 0) {
            throw new IllegalArgumentException(
                    "Monthly income cannot be negative"
            );
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
}