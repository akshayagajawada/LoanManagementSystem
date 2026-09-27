package com.loanmanagement;

import com.loanmanagement.controller.AuthController;
import com.loanmanagement.controller.CustomerController;
import com.loanmanagement.controller.LoanApplicationController;
import com.loanmanagement.controller.LoanController;
import com.loanmanagement.controller.UserController;
import com.loanmanagement.model.Customer;
import com.loanmanagement.model.Loan;
import com.loanmanagement.model.LoanApplication;
import com.loanmanagement.model.User;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        AuthController authController = new AuthController();
        UserController userController = new UserController();
        CustomerController customerController = new CustomerController();
        LoanApplicationController loanApplicationController =
                new LoanApplicationController();
        LoanController loanController = new LoanController();

        Scanner scanner = new Scanner(System.in);

        while (true) {

            System.out.println();
            System.out.println("======================================");
            System.out.println("     LOAN MANAGEMENT SYSTEM");
            System.out.println("======================================");

            System.out.println();
            System.out.println("1. Existing Customer");
            System.out.println("2. New Customer");
            System.out.println("3. Officer Login");
            System.out.println("4. Exit");

            System.out.print("\nEnter your choice: ");

            int choice;

            try {
                choice = scanner.nextInt();
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println("Please enter a valid number.");
                scanner.nextLine();
                continue;
            }

            try {

                switch (choice) {

                    case 1:
                        existingCustomerFlow(
                                authController,
                                customerController,
                                loanApplicationController,
                                scanner
                        );
                        break;

                    case 2:
                        registerCustomer(
                                userController,
                                customerController,
                                scanner
                        );
                        break;

                    case 3:
                        officerLogin(
                                authController,
                                customerController,
                                loanApplicationController,
                                loanController,
                                scanner
                        );
                        break;

                    case 4:
                        System.out.println(
                                "Thank you for using Loan Management System."
                        );
                        scanner.close();
                        return;

                    default:
                        System.out.println(
                                "Invalid choice."
                        );
                }

            } catch (Exception e) {

                System.out.println(
                        "Error: " + e.getMessage()
                );
            }
        }
    }

    // =========================================================
    // EXISTING CUSTOMER
    // =========================================================

    private static void existingCustomerFlow(
            AuthController authController,
            CustomerController customerController,
            LoanApplicationController loanApplicationController,
            Scanner scanner) {

        System.out.println();
        System.out.println("===== EXISTING CUSTOMER LOGIN =====");

        System.out.print("Username: ");
        String username = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        boolean loggedIn =
                authController.login(username, password);

        if (!loggedIn) {

            System.out.println(
                    "Invalid username or password."
            );

            return;
        }

        User user =
                authController.getUserByUsername(username);

        if (user == null) {

            System.out.println(
                    "User details not found."
            );

            return;
        }

        if (!"CUSTOMER".equalsIgnoreCase(
                user.getRole())) {

            System.out.println(
                    "This account is not a customer account."
            );

            return;
        }

        System.out.println();
        System.out.println("Login successful!");
        System.out.println(
                "Logged-in User ID: " + user.getUserId()
        );
        System.out.println(
                "Role: " + user.getRole()
        );

        customerFlow(
                customerController,
                loanApplicationController,
                scanner,
                user.getUserId()
        );
    }

    // =========================================================
    // NEW CUSTOMER REGISTRATION
    // =========================================================

    private static void registerCustomer(
            UserController userController,
            CustomerController customerController,
            Scanner scanner) {

        System.out.println();
        System.out.println("===== NEW CUSTOMER REGISTRATION =====");

        System.out.print("Full Name: ");
        String fullName = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Phone: ");
        String phone = scanner.nextLine();

        System.out.print("Date of Birth (YYYY-MM-DD): ");
        String dob = scanner.nextLine();

        System.out.print("Address: ");
        String address = scanner.nextLine();

        System.out.print("Monthly Income: ");
        double monthlyIncome = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("PAN Number: ");
        String panNumber = scanner.nextLine();

        System.out.print("Aadhaar Last 4 Digits: ");
        String aadhaarLast4 = scanner.nextLine();

        System.out.print(
                "Employment Type (SALARIED/SELF_EMPLOYED): "
        );
        String employmentType = scanner.nextLine();

        System.out.print("Account Number: ");
        String accountNumber = scanner.nextLine();

        System.out.print("IFSC Code: ");
        String ifscCode = scanner.nextLine();

        System.out.print("Bank Name: ");
        String bankName = scanner.nextLine();

        System.out.println();
        System.out.println("----- CREATE LOGIN DETAILS -----");

        System.out.print("Username: ");
        String username = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        // Create User
        User user = new User();

        user.setUsername(username);
        user.setPassword(password);
        user.setRole("CUSTOMER");
        user.setStatus("ACTIVE");

        userController.addUser(user);

        // Create Customer using generated User ID
        Customer customer = new Customer();

        customer.setUserId(
                user.getUserId()
        );

        customer.setFullName(fullName);
        customer.setEmail(email);
        customer.setPhone(phone);
        customer.setDob(dob);
        customer.setAddress(address);
        customer.setMonthlyIncome(monthlyIncome);
        customer.setPanNumber(panNumber);
        customer.setAadhaarLast4(aadhaarLast4);
        customer.setEmploymentType(employmentType);

        customer.setAccountNumber(accountNumber);
        customer.setIfscCode(ifscCode);
        customer.setBankName(bankName);

        customer.setKycStatus("PENDING");
        customer.setStatus("ACTIVE");

        customerController.addCustomer(customer);

        System.out.println();
        System.out.println("======================================");
        System.out.println("     REGISTRATION SUCCESSFUL");
        System.out.println("======================================");

        System.out.println(
                "User ID: " + user.getUserId()
        );

        System.out.println(
                "Customer ID: " + customer.getCustomerId()
        );

        System.out.println(
                "Username: " + user.getUsername()
        );

        System.out.println(
                "KYC Status: " + customer.getKycStatus()
        );

        System.out.println();
        System.out.println(
                "Please wait for officer verification."
        );
    }

    // =========================================================
    // CUSTOMER FLOW
    // =========================================================

    private static void customerFlow(
            CustomerController customerController,
            LoanApplicationController loanApplicationController,
            Scanner scanner,
            int userId) {

        System.out.println();
        System.out.println("===== CUSTOMER =====");

        Customer customer =
                customerController.getCustomerByUserId(userId);

        if (customer == null) {

            System.out.println(
                    "Customer profile not found."
            );

            return;
        }

        System.out.println(
                "Customer ID: " +
                        customer.getCustomerId()
        );

        System.out.println(
                "Customer Name: " +
                        customer.getFullName()
        );

        System.out.println(
                "KYC Status: " +
                        customer.getKycStatus()
        );

        if (!"VERIFIED".equalsIgnoreCase(
                customer.getKycStatus())) {

            System.out.println();
            System.out.println(
                    "Your KYC is not verified yet."
            );

            System.out.println(
                    "Please wait for officer verification."
            );

            return;
        }

        System.out.println();
        System.out.println("===== APPLY FOR LOAN =====");

        System.out.print("Loan Type ID: ");
        int loanTypeId = scanner.nextInt();

        System.out.print("Requested Amount: ");
        double requestedAmount = scanner.nextDouble();

        System.out.print("Tenure (months): ");
        int tenureMonths = scanner.nextInt();

        scanner.nextLine();

        System.out.print("Purpose: ");
        String purpose = scanner.nextLine();

        LoanApplication application =
                new LoanApplication();

        application.setCustomerId(
                customer.getCustomerId()
        );

        application.setLoanTypeId(
                loanTypeId
        );

        application.setRequestedAmount(
                requestedAmount
        );

        application.setTenureMonths(
                tenureMonths
        );

        application.setPurpose(
                purpose
        );

        loanApplicationController.addApplication(
                application
        );

        System.out.println();
        System.out.println(
                "Application ID: " +
                        application.getApplicationId()
        );

        System.out.println(
                "Application Status: " +
                        application.getStatus()
        );

        System.out.println();
        System.out.println(
                "Application submitted for officer approval."
        );
    }

    // =========================================================
    // OFFICER LOGIN
    // =========================================================

    private static void officerLogin(
            AuthController authController,
            CustomerController customerController,
            LoanApplicationController loanApplicationController,
            LoanController loanController,
            Scanner scanner) {

        System.out.println();
        System.out.println("===== OFFICER LOGIN =====");

        System.out.print("Username: ");
        String username = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        boolean loggedIn =
                authController.login(
                        username,
                        password
                );

        if (!loggedIn) {

            System.out.println(
                    "Invalid username or password."
            );

            return;
        }

        User officer =
                authController.getUserByUsername(
                        username
                );

        if (officer == null) {

            System.out.println(
                    "Officer details not found."
            );

            return;
        }

        if (!"LOAN_OFFICER".equalsIgnoreCase(
                officer.getRole())) {

            System.out.println(
                    "This account is not an officer account."
            );

            return;
        }

        System.out.println();
        System.out.println("Login successful!");

        System.out.println(
                "Officer ID: " +
                        officer.getUserId()
        );

        officerFlow(
                authController,
                customerController,
                loanApplicationController,
                loanController,
                scanner,
                officer.getUserId()
        );
    }

    // =========================================================
    // OFFICER FLOW
    // =========================================================

    private static void officerFlow(
            AuthController authController,
            CustomerController customerController,
            LoanApplicationController loanApplicationController,
            LoanController loanController,
            Scanner scanner,
            int officerId) {

        while (true) {

            System.out.println();
            System.out.println("===== OFFICER MENU =====");

            System.out.println(
                    "1. Verify Customer KYC"
            );

            System.out.println(
                    "2. Review Loan Application"
            );

            System.out.println(
                    "3. Logout"
            );

            System.out.print(
                    "Enter your choice: "
            );

            int choice;

            try {
                choice = scanner.nextInt();
                scanner.nextLine();
            } catch (Exception e) {

                System.out.println(
                        "Please enter a valid number."
                );

                scanner.nextLine();
                continue;
            }

            if (choice == 1) {

                verifyCustomerKyc(
                        customerController,
                        scanner,
                        officerId
                );

            } else if (choice == 2) {

                reviewLoanApplication(
                        loanApplicationController,
                        loanController,
                        scanner,
                        officerId
                );

            } else if (choice == 3) {

                authController.logout(officerId);

                System.out.println(
                        "Officer logged out."
                );

                return;

            } else {

                System.out.println(
                        "Invalid choice."
                );
            }
        }
    }

    // =========================================================
    // VERIFY KYC
    // =========================================================

    private static void verifyCustomerKyc(
            CustomerController customerController,
            Scanner scanner,
            int officerId) {

        System.out.println();
        System.out.println("===== VERIFY CUSTOMER KYC =====");

        System.out.print("Enter Customer ID: ");

        int customerId = scanner.nextInt();
        scanner.nextLine();

        Customer customer;

        try {

            customer =
                    customerController.getCustomerById(
                            customerId
                    );

        } catch (Exception e) {

            System.out.println(
                    "Error: " + e.getMessage()
            );

            return;
        }

        if (customer == null) {

            System.out.println(
                    "Customer not found."
            );

            return;
        }

        System.out.println();
        System.out.println(
                "Customer ID: " +
                        customer.getCustomerId()
        );

        System.out.println(
                "Name: " +
                        customer.getFullName()
        );

        System.out.println(
                "Email: " +
                        customer.getEmail()
        );

        System.out.println(
                "Phone: " +
                        customer.getPhone()
        );

        System.out.println(
                "PAN: " +
                        customer.getPanNumber()
        );

        System.out.println(
                "KYC Status: " +
                        customer.getKycStatus()
        );

        if ("VERIFIED".equalsIgnoreCase(
                customer.getKycStatus())) {

            System.out.println(
                    "KYC is already verified."
            );

            return;
        }

        System.out.println();
        System.out.println("1. Verify");
        System.out.println("2. Cancel");

        System.out.print(
                "Enter your choice: "
        );

        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice == 1) {

            customerController.verifyKyc(
                    customerId,
                    officerId
            );

        } else {

            System.out.println(
                    "KYC verification cancelled."
            );
        }
    }

    // =========================================================
    // REVIEW LOAN APPLICATION
    // =========================================================

    private static void reviewLoanApplication(
            LoanApplicationController loanApplicationController,
            LoanController loanController,
            Scanner scanner,
            int officerId) {

        System.out.println();
        System.out.println("===== REVIEW LOAN APPLICATION =====");

        System.out.print("Enter Application ID: ");

        int applicationId =
                scanner.nextInt();

        scanner.nextLine();

        LoanApplication application;

        try {

            application =
                    loanApplicationController.getApplicationById(
                            applicationId
                    );

        } catch (Exception e) {

            System.out.println(
                    "Error: " +
                            e.getMessage()
            );

            return;
        }

        if (application == null) {

            System.out.println(
                    "Application not found."
            );

            return;
        }

        System.out.println();
        System.out.println(
                "Application ID: " +
                        application.getApplicationId()
        );

        System.out.println(
                "Customer ID: " +
                        application.getCustomerId()
        );

        System.out.println(
                "Loan Type ID: " +
                        application.getLoanTypeId()
        );

        System.out.println(
                "Requested Amount: " +
                        application.getRequestedAmount()
        );

        System.out.println(
                "Tenure: " +
                        application.getTenureMonths() +
                        " months"
        );

        System.out.println(
                "Purpose: " +
                        application.getPurpose()
        );

        System.out.println(
                "Status: " +
                        application.getStatus()
        );

        if (!"PENDING".equalsIgnoreCase(
                application.getStatus())) {

            System.out.println();
            System.out.println(
                    "This application has already been reviewed."
            );

            return;
        }

        System.out.println();
        System.out.println("1. Approve");
        System.out.println("2. Reject");

        System.out.print(
                "Enter your choice: "
        );

        int choice =
                scanner.nextInt();

        scanner.nextLine();

        System.out.print(
                "Remarks: "
        );

        String remarks =
                scanner.nextLine();

        if (choice == 1) {

            loanApplicationController.approveApplication(
                    applicationId,
                    officerId,
                    remarks
            );

            Loan loan =
                    loanController.createLoanFromApplication(
                            applicationId,
                            officerId
                    );

            System.out.println();
            System.out.println("===== LOAN CREATED =====");

            System.out.println(
                    "Loan ID: " +
                            loan.getLoanId()
            );

            System.out.println(
                    "Principal Amount: " +
                            loan.getPrincipalAmount()
            );

            System.out.println(
                    "Interest Rate: " +
                            loan.getInterestRate()
            );

            System.out.println(
                    "Total Payable: " +
                            loan.getTotalPayable()
            );

            System.out.println(
                    "Outstanding Amount: " +
                            loan.getOutstandingAmount()
            );

            System.out.println(
                    "Loan Status: " +
                            loan.getStatus()
            );

        } else if (choice == 2) {

            loanApplicationController.rejectApplication(
                    applicationId,
                    officerId,
                    remarks
            );

        } else {

            System.out.println(
                    "Invalid choice."
            );
        }
    }
}