package com.loanmanagement.controller;

import com.loanmanagement.model.Customer;
import com.loanmanagement.service.CustomerService;
import com.loanmanagement.service.impl.CustomerServiceImpl;

public class CustomerController {

    private final CustomerService customerService;

    public CustomerController() {
        this.customerService = new CustomerServiceImpl();
    }

    public void addCustomer(Customer customer) {
        customerService.addCustomer(customer);
    }

    public Customer getCustomerById(int customerId) {
        return customerService.getCustomerById(customerId);
    }

    public Customer getCustomerByUserId(int userId) {
        return customerService.getCustomerByUserId(userId);
    }

    public void updateCustomer(Customer customer) {
        customerService.updateCustomer(customer);
    }

    public void deleteCustomer(int customerId) {
        customerService.deleteCustomer(customerId);
    }

    public void verifyKyc(int customerId, int officerId) {
        customerService.verifyKyc(customerId, officerId);
    }
}