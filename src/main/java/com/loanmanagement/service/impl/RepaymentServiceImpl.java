package com.loanmanagement.service.impl;

import com.loanmanagement.dao.RepaymentDao;
import com.loanmanagement.model.Repayment;
import com.loanmanagement.service.RepaymentService;
import com.loanmanagement.dao.impl.RepaymentDaoImpl;
public class RepaymentServiceImpl implements RepaymentService {

    private final RepaymentDao repaymentDao;

    public RepaymentServiceImpl() {
        this.repaymentDao = new RepaymentDaoImpl();
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

        repaymentDao.addRepayment(repayment);
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