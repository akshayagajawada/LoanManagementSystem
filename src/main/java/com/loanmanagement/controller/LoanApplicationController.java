package com.loanmanagement.controller;

import com.loanmanagement.model.LoanApplication;
import com.loanmanagement.service.ApplicationService;
import com.loanmanagement.service.impl.ApplicationServiceImpl;

public class LoanApplicationController {

    private final ApplicationService applicationService;

    public LoanApplicationController() {
        this.applicationService = new ApplicationServiceImpl();
    }

    public void addApplication(LoanApplication application) {
        applicationService.addApplication(application);
    }

    public LoanApplication getApplicationById(int applicationId) {
        return applicationService.getApplicationById(applicationId);
    }

    public void updateApplication(LoanApplication application) {
        applicationService.updateApplication(application);
    }

    public void deleteApplication(int applicationId) {
        applicationService.deleteApplication(applicationId);
    }

    public void approveApplication(
            int applicationId,
            int officerId,
            String remarks) {

        applicationService.approveApplication(
                applicationId,
                officerId,
                remarks
        );
    }

    public void rejectApplication(
            int applicationId,
            int officerId,
            String remarks) {

        applicationService.rejectApplication(
                applicationId,
                officerId,
                remarks
        );
    }
}