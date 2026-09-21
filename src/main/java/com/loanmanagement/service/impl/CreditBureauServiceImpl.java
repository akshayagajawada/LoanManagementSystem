package com.loanmanagement.service.impl;

import com.loanmanagement.dao.CustomerDao;
import com.loanmanagement.dao.impl.CustomerDaoImpl;
import com.loanmanagement.model.Customer;
import com.loanmanagement.service.CreditBureauService;

public class CreditBureauServiceImpl implements CreditBureauService {

    private final CustomerDao customerDao;

    public CreditBureauServiceImpl() {
        this.customerDao = new CustomerDaoImpl();
    }

    @Override
    public int getCreditScore(int customerId) {

        if (customerId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid customer ID"
            );
        }

        Customer customer = customerDao.getCustomerById(customerId);

        if (customer == null) {
            throw new IllegalArgumentException(
                    "Customer not found"
            );
        }

        if (customer.getPanNumber() == null ||
                customer.getPanNumber().isBlank()) {
            throw new IllegalArgumentException(
                    "PAN number is required to fetch credit score"
            );
        }

        String pan = customer.getPanNumber().toUpperCase();

        int hash = 0;

        for (int i = 0; i < pan.length(); i++) {
            hash = (hash * 31 + pan.charAt(i)) % 100000;
        }

        int score = 580 + (hash % 321);

        // Store the fetched score in the customer record.
        customer.setCreditScore(score);

        // Simulated existing EMI, matching the prototype behavior.
        double existingEmi = (hash % 9) * 1500.0;

        customer.setExistingEmi(existingEmi);

        customerDao.updateCustomer(customer);

        return score;
    }

    @Override
    public boolean isEligible(int customerId) {

        int score = getCreditScore(customerId);

        /*
         * This is only a bureau recommendation.
         * It does NOT automatically approve the loan.
         *
         * 700+ = bureau recommendation is "Eligible"
         * 650-699 = Refer
         * 300-649 = Decline recommended
         */
        return score >= 700;
    }
}