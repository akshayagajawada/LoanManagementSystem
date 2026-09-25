package com.loanmanagement.service.impl;

import com.loanmanagement.model.LoanApplication;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ApplicationServiceImplTest {

    @Test
    void addApplicationShouldRejectNullApplication() {
        ApplicationServiceImpl service = new ApplicationServiceImpl();

        assertThrows(
                IllegalArgumentException.class,
                () -> service.addApplication(null)
        );
    }

    @Test
    void addApplicationShouldRejectInvalidCustomerId() {
        ApplicationServiceImpl service = new ApplicationServiceImpl();

        LoanApplication application = new LoanApplication();
        application.setCustomerId(0);

        assertThrows(
                IllegalArgumentException.class,
                () -> service.addApplication(application)
        );
    }

    @Test
    void approveApplicationShouldRejectInvalidApplicationId() {
        ApplicationServiceImpl service = new ApplicationServiceImpl();

        assertThrows(
                IllegalArgumentException.class,
                () -> service.approveApplication(0, 1, "Approved")
        );
    }

    @Test
    void rejectApplicationShouldRejectInvalidApplicationId() {
        ApplicationServiceImpl service = new ApplicationServiceImpl();

        assertThrows(
                IllegalArgumentException.class,
                () -> service.rejectApplication(0, 1, "Documents not valid")
        );
    }

    @Test
    void rejectApplicationShouldRejectEmptyRemarks() {
        ApplicationServiceImpl service = new ApplicationServiceImpl();

        assertThrows(
                IllegalArgumentException.class,
                () -> service.rejectApplication(1, 1, "")
        );
    }
}