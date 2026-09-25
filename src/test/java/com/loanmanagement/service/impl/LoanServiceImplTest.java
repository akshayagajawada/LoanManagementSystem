package com.loanmanagement.service.impl;

import com.loanmanagement.model.Loan;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoanServiceImplTest {

    @Test
    void addLoanShouldRejectNullLoan() {
        LoanServiceImpl service = new LoanServiceImpl();

        assertThrows(
                IllegalArgumentException.class,
                () -> service.addLoan(null)
        );
    }

    @Test
    void addLoanShouldRejectInvalidApplicationId() {
        LoanServiceImpl service = new LoanServiceImpl();

        Loan loan = new Loan();
        loan.setApplicationId(0);

        assertThrows(
                IllegalArgumentException.class,
                () -> service.addLoan(loan)
        );
    }

    @Test
    void addLoanShouldRejectInvalidCustomerId() {
        LoanServiceImpl service = new LoanServiceImpl();

        Loan loan = new Loan();
        loan.setApplicationId(1);
        loan.setCustomerId(0);

        assertThrows(
                IllegalArgumentException.class,
                () -> service.addLoan(loan)
        );
    }

    @Test
    void addLoanShouldRejectInvalidPrincipalAmount() {
        LoanServiceImpl service = new LoanServiceImpl();

        Loan loan = new Loan();
        loan.setApplicationId(1);
        loan.setCustomerId(1);
        loan.setLoanTypeId(1);
        loan.setPrincipalAmount(0);

        assertThrows(
                IllegalArgumentException.class,
                () -> service.addLoan(loan)
        );
    }

    @Test
    void addLoanShouldRejectInvalidTenure() {
        LoanServiceImpl service = new LoanServiceImpl();

        Loan loan = new Loan();
        loan.setApplicationId(1);
        loan.setCustomerId(1);
        loan.setLoanTypeId(1);
        loan.setPrincipalAmount(100000);
        loan.setTenureMonths(0);

        assertThrows(
                IllegalArgumentException.class,
                () -> service.addLoan(loan)
        );
    }
}