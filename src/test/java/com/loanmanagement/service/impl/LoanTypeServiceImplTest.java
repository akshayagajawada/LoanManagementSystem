package com.loanmanagement.service.impl;

import com.loanmanagement.model.LoanType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoanTypeServiceImplTest {

    @Test
    void addLoanTypeShouldRejectNullLoanType() {
        LoanTypeServiceImpl service = new LoanTypeServiceImpl();

        assertThrows(
                IllegalArgumentException.class,
                () -> service.addLoanType(null)
        );
    }

    @Test
    void addLoanTypeShouldRejectEmptyName() {
        LoanTypeServiceImpl service = new LoanTypeServiceImpl();

        LoanType loanType = new LoanType();
        loanType.setName("");

        assertThrows(
                IllegalArgumentException.class,
                () -> service.addLoanType(loanType)
        );
    }

    @Test
    void addLoanTypeShouldRejectNegativeInterestRate() {
        LoanTypeServiceImpl service = new LoanTypeServiceImpl();

        LoanType loanType = new LoanType();
        loanType.setName("Personal Loan");
        loanType.setInterestRate(-5);

        assertThrows(
                IllegalArgumentException.class,
                () -> service.addLoanType(loanType)
        );
    }

    @Test
    void addLoanTypeShouldRejectInvalidAmountRange() {
        LoanTypeServiceImpl service = new LoanTypeServiceImpl();

        LoanType loanType = new LoanType();
        loanType.setName("Personal Loan");
        loanType.setInterestRate(10);
        loanType.setMinAmount(100000);
        loanType.setMaxAmount(50000);

        assertThrows(
                IllegalArgumentException.class,
                () -> service.addLoanType(loanType)
        );
    }
}