package com.loanmanagement.service.impl;

import com.loanmanagement.dao.LoanDao;
import com.loanmanagement.dao.RepaymentDao;
import com.loanmanagement.dao.impl.LoanDaoImpl;
import com.loanmanagement.dao.impl.RepaymentDaoImpl;
import com.loanmanagement.model.Loan;
import com.loanmanagement.model.Repayment;
import com.loanmanagement.service.RepaymentService;

public class RepaymentServiceImpl implements RepaymentService {

    private final RepaymentDao repaymentDao;
    private final LoanDao loanDao;

    public RepaymentServiceImpl() {
        this.repaymentDao = new RepaymentDaoImpl();
        this.loanDao = new LoanDaoImpl();
    }

    @Override
    public void addRepayment(Repayment repayment) {

        if (repayment == null) {
            throw new IllegalArgumentException(
                    "Repayment cannot be null"
            );
        }

        if (repayment.getLoanId() <= 0) {
            throw new IllegalArgumentException(
                    "Invalid loan ID"
            );
        }

        if (repayment.getAmount() <= 0) {
            throw new IllegalArgumentException(
                    "Repayment amount must be greater than zero"
            );
        }

        if (repayment.getPaymentMode() == null ||
                repayment.getPaymentMode().isBlank()) {

            throw new IllegalArgumentException(
                    "Payment mode cannot be empty"
            );
        }

        // Get the loan
        Loan loan = loanDao.getLoanById(
                repayment.getLoanId()
        );

        if (loan == null) {
            throw new IllegalArgumentException(
                    "Loan not found"
            );
        }

        // Repayment is allowed only for active loans
        if (!"ACTIVE".equalsIgnoreCase(loan.getStatus())) {
            throw new IllegalArgumentException(
                    "Repayment cannot be made for a closed loan"
            );
        }

        // Repayment cannot be greater than outstanding amount
        if (repayment.getAmount() >
                loan.getOutstandingAmount()) {

            throw new IllegalArgumentException(
                    "Repayment amount cannot exceed outstanding amount"
            );
        }

        // Save repayment
        repaymentDao.addRepayment(repayment);

        // Calculate remaining outstanding amount
        double remainingAmount =
                loan.getOutstandingAmount()
                        - repayment.getAmount();

        // Avoid very small floating-point values
        if (remainingAmount < 0.01) {
            remainingAmount = 0;
        }

        loan.setOutstandingAmount(remainingAmount);

        // Close loan when fully paid
        if (remainingAmount == 0) {
            loan.setStatus("CLOSED");

            System.out.println(
                    "Loan fully repaid. Loan closed successfully."
            );
        }

        // Update loan
        loanDao.updateLoan(loan);

        System.out.println(
                "Repayment recorded successfully."
        );
    }

    @Override
    public Repayment getRepaymentById(int repaymentId) {

        if (repaymentId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid repayment ID"
            );
        }

        return repaymentDao.getRepaymentById(repaymentId);
    }

    @Override
    public void updateRepayment(Repayment repayment) {

        if (repayment == null) {
            throw new IllegalArgumentException(
                    "Repayment cannot be null"
            );
        }

        if (repayment.getRepaymentId() <= 0) {
            throw new IllegalArgumentException(
                    "Invalid repayment ID"
            );
        }

        if (repayment.getAmount() <= 0) {
            throw new IllegalArgumentException(
                    "Repayment amount must be greater than zero"
            );
        }

        repaymentDao.updateRepayment(repayment);
    }

    @Override
    public void deleteRepayment(int repaymentId) {

        if (repaymentId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid repayment ID"
            );
        }

        repaymentDao.deleteRepayment(repaymentId);
    }
}