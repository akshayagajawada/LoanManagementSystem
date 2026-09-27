package com.loanmanagement.dao;

import com.loanmanagement.model.Customer;

public interface CustomerDao {

    void addCustomer(Customer customer);

    Customer getCustomerById(int customerId);

    Customer getCustomerByUserId(int userId);

    void updateCustomer(Customer customer);

    void deleteCustomer(int customerId);

    void verifyKyc(int customerId, int officerId);
}