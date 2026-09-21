package com.loanmanagement.dao;

import com.loanmanagement.model.Repayment;

public interface RepaymentDao {

    void addRepayment(Repayment repayment);

    Repayment getRepaymentById(int repaymentId);

    void updateRepayment(Repayment repayment);

    void deleteRepayment(int repaymentId);
}