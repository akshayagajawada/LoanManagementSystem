package com.loanmanagement.service;

import com.loanmanagement.model.Loan;

public interface LoanService {

    void addLoan(Loan loan);

    Loan getLoanById(int loanId);

    void updateLoan(Loan loan);

    void deleteLoan(int loanId);

    Loan createLoanFromApplication(int applicationId, int createdBy);
}