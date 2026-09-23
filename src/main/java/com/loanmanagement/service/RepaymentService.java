package com.loanmanagement.service;

import com.loanmanagement.model.Repayment;

public interface RepaymentService {

    void addRepayment(Repayment repayment);

    Repayment getRepaymentById(int repaymentId);

    void updateRepayment(Repayment repayment);

    void deleteRepayment(int repaymentId);
}