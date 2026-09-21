package com.loanmanagement.service.impl;

import com.loanmanagement.dao.LoanDao;
import com.loanmanagement.dao.impl.LoanDaoImpl;
import com.loanmanagement.model.Loan;
import com.loanmanagement.service.LoanService;

public class LoanServiceImpl implements LoanService {

    private final LoanDao loanDao;

    public LoanServiceImpl() {
        this.loanDao = new LoanDaoImpl();
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