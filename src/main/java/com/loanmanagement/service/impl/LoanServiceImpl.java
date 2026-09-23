package com.loanmanagement.service.impl;

import com.loanmanagement.dao.LoanApplicationDao;
import com.loanmanagement.dao.LoanDao;
import com.loanmanagement.dao.LoanTypeDao;

import com.loanmanagement.dao.impl.LoanApplicationDaoImpl;
import com.loanmanagement.dao.impl.LoanDaoImpl;
import com.loanmanagement.dao.impl.LoanTypeDaoImpl;

import com.loanmanagement.model.Loan;
import com.loanmanagement.model.LoanApplication;
import com.loanmanagement.model.LoanType;

import com.loanmanagement.service.LoanService;

public class LoanServiceImpl implements LoanService {

    private final LoanDao loanDao;
    private final LoanApplicationDao loanApplicationDao;
    private final LoanTypeDao loanTypeDao;

    public LoanServiceImpl() {
        this.loanDao = new LoanDaoImpl();
        this.loanApplicationDao = new LoanApplicationDaoImpl();
        this.loanTypeDao = new LoanTypeDaoImpl();
    }

    @Override
    public void addLoan(Loan loan) {

        if (loan == null) {
            throw new IllegalArgumentException(
                    "Loan cannot be null"
            );
        }

        if (loan.getApplicationId() <= 0) {
            throw new IllegalArgumentException(
                    "Invalid application ID"
            );
        }

        if (loan.getCustomerId() <= 0) {
            throw new IllegalArgumentException(
                    "Invalid customer ID"
            );
        }

        if (loan.getLoanTypeId() <= 0) {
            throw new IllegalArgumentException(
                    "Invalid loan type ID"
            );
        }

        if (loan.getPrincipalAmount() <= 0) {
            throw new IllegalArgumentException(
                    "Principal amount must be greater than zero"
            );
        }

        if (loan.getInterestRate() < 0) {
            throw new IllegalArgumentException(
                    "Interest rate cannot be negative"
            );
        }

        if (loan.getTenureMonths() <= 0) {
            throw new IllegalArgumentException(
                    "Tenure must be greater than zero"
            );
        }

        if (loan.getTotalPayable() < 0) {
            throw new IllegalArgumentException(
                    "Total payable cannot be negative"
            );
        }

        if (loan.getOutstandingAmount() < 0) {
            throw new IllegalArgumentException(
                    "Outstanding amount cannot be negative"
            );
        }

        if (loan.getStatus() == null ||
                loan.getStatus().isBlank()) {

            loan.setStatus("ACTIVE");
        }

        loanDao.addLoan(loan);
    }

    @Override
    public Loan createLoanFromApplication(
            int applicationId,
            int createdBy) {

        if (applicationId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid application ID"
            );
        }

        if (createdBy <= 0) {
            throw new IllegalArgumentException(
                    "Invalid created by user ID"
            );
        }

        // Get the loan application
        LoanApplication application =
                loanApplicationDao.getLoanApplicationById(applicationId);

        if (application == null) {
            throw new IllegalArgumentException(
                    "Loan application not found"
            );
        }

        // Only approved applications can become loans
        if (!"APPROVED".equalsIgnoreCase(application.getStatus())) {
            throw new IllegalArgumentException(
                    "Only approved applications can be converted into loans"
            );
        }

        // Get the selected loan type
        LoanType loanType =
                loanTypeDao.getLoanTypeById(
                        application.getLoanTypeId()
                );

        if (loanType == null) {
            throw new IllegalArgumentException(
                    "Loan type not found"
            );
        }

        // Calculate simple total payable
        double principal =
                application.getRequestedAmount();

        double interest =
                principal
                        * loanType.getInterestRate()
                        / 100;

        double totalPayable =
                principal + interest;

        // Create loan
        Loan loan = new Loan();

        loan.setApplicationId(
                application.getApplicationId()
        );

        loan.setCustomerId(
                application.getCustomerId()
        );

        loan.setLoanTypeId(
                application.getLoanTypeId()
        );

        loan.setPrincipalAmount(principal);

        loan.setInterestRate(
                loanType.getInterestRate()
        );

        loan.setTenureMonths(
                application.getTenureMonths()
        );

        loan.setTotalPayable(totalPayable);

        loan.setOutstandingAmount(totalPayable);

        loan.setStatus("ACTIVE");

        loan.setCreatedBy(createdBy);

        // Save loan
        loanDao.addLoan(loan);

        System.out.println(
                "Loan created successfully."
        );

        return loan;
    }

    @Override
    public Loan getLoanById(int loanId) {

        if (loanId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid loan ID"
            );
        }

        return loanDao.getLoanById(loanId);
    }

    @Override
    public void updateLoan(Loan loan) {

        if (loan == null) {
            throw new IllegalArgumentException(
                    "Loan cannot be null"
            );
        }

        if (loan.getLoanId() <= 0) {
            throw new IllegalArgumentException(
                    "Invalid loan ID"
            );
        }

        if (loan.getOutstandingAmount() < 0) {
            throw new IllegalArgumentException(
                    "Outstanding amount cannot be negative"
            );
        }

        loanDao.updateLoan(loan);
    }

    @Override
    public void deleteLoan(int loanId) {

        if (loanId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid loan ID"
            );
        }

        loanDao.deleteLoan(loanId);
    }
}