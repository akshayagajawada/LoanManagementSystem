package com.loanmanagement.util;

import com.loanmanagement.service.AuthService;
import com.loanmanagement.service.impl.AuthServiceImpl;

public class LoginTest {

    public static void main(String[] args) {

        AuthService authService = new AuthServiceImpl();

        boolean result = authService.login("admin", "admin123");

        if (result) {
            System.out.println("Login successful!");
        } else {
            System.out.println("Login failed!");
        }
    }
}