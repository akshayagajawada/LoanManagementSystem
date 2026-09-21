package com.loanmanagement.service.impl;

import com.loanmanagement.dao.LoanTypeDao;
import com.loanmanagement.dao.impl.LoanTypeDaoImpl;
import com.loanmanagement.model.LoanType;
import com.loanmanagement.service.LoanTypeService;

public class LoanTypeServiceImpl implements LoanTypeService {

    private final LoanTypeDao loanTypeDao;

    public LoanTypeServiceImpl() {
        this.loanTypeDao = new LoanTypeDaoImpl();
    }

    @Override
    public void addLoanType(LoanType loanType) {

        if (loanType == null) {
            throw new IllegalArgumentException("Loan type cannot be null");
        }

        if (loanType.getName() == null ||
                loanType.getName().isBlank()) {
            throw new IllegalArgumentException("Loan type name cannot be empty");
        }

        if (loanType.getInterestRate() < 0) {
            throw new IllegalArgumentException(
                    "Interest rate cannot be negative"
            );
        }

        if (loanType.getMinAmount() < 0 ||
                loanType.getMaxAmount() < 0) {
            throw new IllegalArgumentException(
                    "Loan amount cannot be negative"
            );
        }

        if (loanType.getMinAmount() > loanType.getMaxAmount()) {
            throw new IllegalArgumentException(
                    "Minimum amount cannot be greater than maximum amount"
            );
        }

        if (loanType.getMaxTenureMonths() <= 0) {
            throw new IllegalArgumentException(
                    "Maximum tenure must be greater than zero"
            );
        }

        loanTypeDao.addLoanType(loanType);
    }

    @Override
    public LoanType getLoanTypeById(int loanTypeId) {

        if (loanTypeId <= 0) {
            throw new IllegalArgumentException("Invalid loan type ID");
        }

        return loanTypeDao.getLoanTypeById(loanTypeId);
    }

    @Override
    public void updateLoanType(LoanType loanType) {

        if (loanType == null) {
            throw new IllegalArgumentException("Loan type cannot be null");
        }

        if (loanType.getLoanTypeId() <= 0) {
            throw new IllegalArgumentException("Invalid loan type ID");
        }

        if (loanType.getName() == null ||
                loanType.getName().isBlank()) {
            throw new IllegalArgumentException("Loan type name cannot be empty");
        }

        if (loanType.getMinAmount() > loanType.getMaxAmount()) {
            throw new IllegalArgumentException(
                    "Minimum amount cannot be greater than maximum amount"
            );
        }

        loanTypeDao.updateLoanType(loanType);
    }

    @Override
    public void deleteLoanType(int loanTypeId) {

        if (loanTypeId <= 0) {
            throw new IllegalArgumentException("Invalid loan type ID");
        }

        loanTypeDao.deleteLoanType(loanTypeId);
    }
}