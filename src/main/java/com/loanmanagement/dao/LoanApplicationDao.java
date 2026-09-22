package com.loanmanagement.dao;

import com.loanmanagement.model.LoanApplication;

public interface LoanApplicationDao {

    void addLoanApplication(LoanApplication application);

    LoanApplication getLoanApplicationById(int applicationId);

    void updateLoanApplication(LoanApplication application);

    void deleteLoanApplication(int applicationId);
}