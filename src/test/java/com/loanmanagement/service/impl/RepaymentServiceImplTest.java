package com.loanmanagement.service.impl;

import com.loanmanagement.model.Repayment;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RepaymentServiceImplTest {

    @Test
    void addRepaymentShouldRejectNullRepayment() {
        RepaymentServiceImpl service = new RepaymentServiceImpl();

        assertThrows(
                IllegalArgumentException.class,
                () -> service.addRepayment(null)
        );
    }

    @Test
    void addRepaymentShouldRejectInvalidLoanId() {
        RepaymentServiceImpl service = new RepaymentServiceImpl();

        Repayment repayment = new Repayment();
        repayment.setLoanId(0);

        assertThrows(
                IllegalArgumentException.class,
                () -> service.addRepayment(repayment)
        );
    }

    @Test
    void addRepaymentShouldRejectInvalidAmount() {
        RepaymentServiceImpl service = new RepaymentServiceImpl();

        Repayment repayment = new Repayment();
        repayment.setLoanId(1);
        repayment.setAmount(0);

        assertThrows(
                IllegalArgumentException.class,
                () -> service.addRepayment(repayment)
        );
    }

    @Test
    void addRepaymentShouldRejectEmptyPaymentMode() {
        RepaymentServiceImpl service = new RepaymentServiceImpl();

        Repayment repayment = new Repayment();
        repayment.setLoanId(1);
        repayment.setAmount(5000);
        repayment.setPaymentMode("");

        assertThrows(
                IllegalArgumentException.class,
                () -> service.addRepayment(repayment)
        );
    }
}