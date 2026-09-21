package com.loanmanagement.service;

import com.loanmanagement.model.LoanApplication;

public interface ApplicationService {

    void addApplication(LoanApplication application);

    LoanApplication getApplicationById(int applicationId);

    void updateApplication(LoanApplication application);

    void deleteApplication(int applicationId);
}