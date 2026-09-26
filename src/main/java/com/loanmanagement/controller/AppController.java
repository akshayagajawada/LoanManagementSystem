package com.loanmanagement.controller;

import com.loanmanagement.model.Customer;
import com.loanmanagement.model.Loan;
import com.loanmanagement.model.LoanApplication;
import com.loanmanagement.model.LoanType;
import com.loanmanagement.model.User;

import com.loanmanagement.service.ApplicationService;
import com.loanmanagement.service.AuthService;
import com.loanmanagement.service.CustomerService;
import com.loanmanagement.service.LoanService;
import com.loanmanagement.service.LoanTypeService;
import com.loanmanagement.service.UserService;

import com.loanmanagement.service.impl.ApplicationServiceImpl;
import com.loanmanagement.service.impl.AuthServiceImpl;
import com.loanmanagement.service.impl.CustomerServiceImpl;
import com.loanmanagement.service.impl.LoanServiceImpl;
import com.loanmanagement.service.impl.LoanTypeServiceImpl;
import com.loanmanagement.service.impl.UserServiceImpl;

import java.util.List;

public class AppController {

    private final AuthService authService;
    private final UserService userService;
    private final CustomerService customerService;
    private final LoanTypeService loanTypeService;
    private final ApplicationService applicationService;
    private final LoanService loanService;

    public AppController() {

        this.authService = new AuthServiceImpl();
        this.userService = new UserServiceImpl();
        this.customerService = new CustomerServiceImpl();
        this.loanTypeService = new LoanTypeServiceImpl();
        this.applicationService = new ApplicationServiceImpl();
        this.loanService = new LoanServiceImpl();
    }

    // =========================
    // AUTHENTICATION
    // =========================

    public boolean login(String username, String password) {
        return authService.login(username, password);
    }

    public User getUserByUsername(String username) {
        return authService.getUserByUsername(username);
    }

    public void logout(int userId) {
        authService.logout(userId);
    }


    // =========================
    // USER CRUD
    // =========================

    public void addUser(User user) {
        userService.addUser(user);
    }

    public User getUserById(int userId) {
        return userService.getUserById(userId);
    }

    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    public void updateUser(User user) {
        userService.updateUser(user);
    }

    public void deleteUser(int userId) {
        userService.deleteUser(userId);
    }


    // =========================
    // CUSTOMER CRUD
    // =========================

    public void addCustomer(Customer customer) {
        customerService.addCustomer(customer);
    }

    public Customer getCustomerById(int customerId) {
        return customerService.getCustomerById(customerId);
    }

    public void updateCustomer(Customer customer) {
        customerService.updateCustomer(customer);
    }

    public void deleteCustomer(int customerId) {
        customerService.deleteCustomer(customerId);
    }


    // =========================
    // LOAN TYPE CRUD
    // =========================

    public void addLoanType(LoanType loanType) {
        loanTypeService.addLoanType(loanType);
    }

    public LoanType getLoanTypeById(int loanTypeId) {
        return loanTypeService.getLoanTypeById(loanTypeId);
    }

    public void updateLoanType(LoanType loanType) {
        loanTypeService.updateLoanType(loanType);
    }

    public void deleteLoanType(int loanTypeId) {
        loanTypeService.deleteLoanType(loanTypeId);
    }


    // =========================
    // LOAN APPLICATION
    // =========================

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


    // =========================
    // APPLICATION APPROVAL
    // =========================

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


    // =========================
    // APPLICATION REJECTION
    // =========================

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


    // =========================
    // LOAN CRUD
    // =========================

    public void addLoan(Loan loan) {
        loanService.addLoan(loan);
    }

    public Loan getLoanById(int loanId) {
        return loanService.getLoanById(loanId);
    }

    public void updateLoan(Loan loan) {
        loanService.updateLoan(loan);
    }

    public void deleteLoan(int loanId) {
        loanService.deleteLoan(loanId);
    }


    // =========================
    // CREATE LOAN FROM APPROVED APPLICATION
    // =========================

    public Loan createLoanFromApplication(
            int applicationId,
            int createdBy) {

        return loanService.createLoanFromApplication(
                applicationId,
                createdBy
        );
    }
}