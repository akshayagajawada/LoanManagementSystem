package com.loanmanagement.controller;

import com.loanmanagement.model.Loan;
import com.loanmanagement.service.LoanService;
import com.loanmanagement.service.impl.LoanServiceImpl;

public class LoanController {

    private final LoanService loanService;

    public LoanController() {
        this.loanService = new LoanServiceImpl();
    }

    public void addLoan(Loan loan) {
        loanService.addLoan(loan);
    }

    public Loan getLoanById(int loanId) {
        return loanService.getLoanById(loanId);
    }

    public void updateLoan(Loan loan) {
        loanService.updateLoan(loan);
    }

    public void deleteLoan(int loanId) {
        loanService.deleteLoan(loanId);
    }

    public Loan createLoanFromApplication(
            int applicationId,
            int createdBy) {

        return loanService.createLoanFromApplication(
                applicationId,
                createdBy
        );
    }
}