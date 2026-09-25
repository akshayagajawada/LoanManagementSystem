package com.loanmanagement.service.impl;

import com.loanmanagement.model.Customer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomerServiceImplTest {

    @Test
    void addCustomerShouldRejectNullCustomer() {
        CustomerServiceImpl service = new CustomerServiceImpl();

        assertThrows(
                IllegalArgumentException.class,
                () -> service.addCustomer(null)
        );
    }

    @Test
    void addCustomerShouldRejectInvalidCustomerName() {
        CustomerServiceImpl service = new CustomerServiceImpl();

        Customer customer = new Customer();
        customer.setFullName("");

        assertThrows(
                IllegalArgumentException.class,
                () -> service.addCustomer(customer)
        );
    }

    @Test
    void addCustomerShouldRejectInvalidEmail() {
        CustomerServiceImpl service = new CustomerServiceImpl();

        Customer customer = new Customer();
        customer.setFullName("Test Customer");
        customer.setEmail("invalid-email");

        assertThrows(
                IllegalArgumentException.class,
                () -> service.addCustomer(customer)
        );
    }

    @Test
    void addCustomerShouldRejectInvalidPhone() {
        CustomerServiceImpl service = new CustomerServiceImpl();

        Customer customer = new Customer();
        customer.setFullName("Test Customer");
        customer.setEmail("test@example.com");
        customer.setPhone("123");

        assertThrows(
                IllegalArgumentException.class,
                () -> service.addCustomer(customer)
        );
    }
}