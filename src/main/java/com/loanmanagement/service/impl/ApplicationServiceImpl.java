package com.loanmanagement.service.impl;

import com.loanmanagement.dao.CustomerDao;
import com.loanmanagement.dao.LoanApplicationDao;
import com.loanmanagement.dao.LoanTypeDao;

import com.loanmanagement.dao.impl.CustomerDaoImpl;
import com.loanmanagement.dao.impl.LoanApplicationDaoImpl;
import com.loanmanagement.dao.impl.LoanTypeDaoImpl;

import com.loanmanagement.model.Customer;
import com.loanmanagement.model.LoanApplication;
import com.loanmanagement.model.LoanType;

import com.loanmanagement.service.ApplicationService;
import com.loanmanagement.service.CreditBureauService;
import com.loanmanagement.service.impl.CreditBureauServiceImpl;

public class ApplicationServiceImpl implements ApplicationService {

    private final LoanApplicationDao loanApplicationDao;
    private final CustomerDao customerDao;
    private final LoanTypeDao loanTypeDao;
    private final CreditBureauService creditBureauService;

    public ApplicationServiceImpl() {

        this.loanApplicationDao =
                new LoanApplicationDaoImpl();

        this.customerDao =
                new CustomerDaoImpl();

        this.loanTypeDao =
                new LoanTypeDaoImpl();

        this.creditBureauService =
                new CreditBureauServiceImpl();
    }

    @Override
    public void addApplication(LoanApplication application) {

        if (application == null) {
            throw new IllegalArgumentException(
                    "Loan application cannot be null"
            );
        }

        // --------------------------------------------------------
        // 1. Validate customer
        // --------------------------------------------------------

        if (application.getCustomerId() <= 0) {
            throw new IllegalArgumentException(
                    "Invalid customer ID"
            );
        }

        Customer customer =
                customerDao.getCustomerById(
                        application.getCustomerId()
                );

        if (customer == null) {
            throw new IllegalArgumentException(
                    "Customer not found"
            );
        }

        // --------------------------------------------------------
        // 2. Customer must have VERIFIED KYC
        // --------------------------------------------------------

        if (!"VERIFIED".equalsIgnoreCase(
                customer.getKycStatus()
        )) {

            throw new IllegalArgumentException(
                    "KYC must be VERIFIED before applying for a loan"
            );
        }

        // --------------------------------------------------------
        // 3. Validate loan type
        // --------------------------------------------------------

        if (application.getLoanTypeId() <= 0) {
            throw new IllegalArgumentException(
                    "Invalid loan type ID"
            );
        }

        LoanType loanType =
                loanTypeDao.getLoanTypeById(
                        application.getLoanTypeId()
                );

        if (loanType == null) {
            throw new IllegalArgumentException(
                    "Loan type not found"
            );
        }

        // --------------------------------------------------------
        // 4. Loan type must be ACTIVE
        // --------------------------------------------------------

        if (!"ACTIVE".equalsIgnoreCase(
                loanType.getStatus()
        )) {

            throw new IllegalArgumentException(
                    "Selected loan type is inactive"
            );
        }

        // --------------------------------------------------------
        // 5. Validate requested amount
        // --------------------------------------------------------

        if (application.getRequestedAmount() <= 0) {
            throw new IllegalArgumentException(
                    "Requested amount must be greater than zero"
            );
        }

        if (application.getRequestedAmount()
                < loanType.getMinAmount()) {

            throw new IllegalArgumentException(
                    "Requested amount is below the minimum loan amount"
            );
        }

        if (application.getRequestedAmount()
                > loanType.getMaxAmount()) {

            throw new IllegalArgumentException(
                    "Requested amount exceeds the maximum loan amount"
            );
        }

        // --------------------------------------------------------
        // 6. Validate tenure
        // --------------------------------------------------------

        if (application.getTenureMonths() <= 0) {
            throw new IllegalArgumentException(
                    "Tenure must be greater than zero"
            );
        }

        if (application.getTenureMonths()
                > loanType.getMaxTenureMonths()) {

            throw new IllegalArgumentException(
                    "Requested tenure exceeds the maximum allowed tenure"
            );
        }

        // --------------------------------------------------------
        // 7. Validate purpose
        // --------------------------------------------------------

        if (application.getPurpose() == null ||
                application.getPurpose().isBlank()) {

            throw new IllegalArgumentException(
                    "Loan purpose cannot be empty"
            );
        }

        // --------------------------------------------------------
        // 8. Credit assessment
        // --------------------------------------------------------

        int creditScore =
                creditBureauService.getCreditScore(
                        application.getCustomerId()
                );

        System.out.println(
                "Credit score for customer "
                        + application.getCustomerId()
                        + ": "
                        + creditScore
        );

        // --------------------------------------------------------
        // 9. New applications always start as PENDING
        // --------------------------------------------------------

        application.setStatus("PENDING");

        // --------------------------------------------------------
        // 10. Save application
        // --------------------------------------------------------

        loanApplicationDao.addLoanApplication(
                application
        );

        System.out.println(
                "Loan application created successfully."
        );
    }

    @Override
    public LoanApplication getApplicationById(
            int applicationId) {

        if (applicationId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid application ID"
            );
        }

        LoanApplication application =
                loanApplicationDao.getLoanApplicationById(
                        applicationId
                );

        if (application == null) {
            throw new IllegalArgumentException(
                    "Loan application not found"
            );
        }

        return application;
    }

    @Override
    public void updateApplication(
            LoanApplication application) {

        if (application == null) {
            throw new IllegalArgumentException(
                    "Loan application cannot be null"
            );
        }

        if (application.getApplicationId() <= 0) {
            throw new IllegalArgumentException(
                    "Invalid application ID"
            );
        }

        if (application.getRequestedAmount() <= 0) {
            throw new IllegalArgumentException(
                    "Requested amount must be greater than zero"
            );
        }

        if (application.getTenureMonths() <= 0) {
            throw new IllegalArgumentException(
                    "Tenure must be greater than zero"
            );
        }

        loanApplicationDao.updateLoanApplication(
                application
        );
    }

    @Override
    public void deleteApplication(
            int applicationId) {

        if (applicationId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid application ID"
            );
        }

        loanApplicationDao.deleteLoanApplication(
                applicationId
        );
    }
}