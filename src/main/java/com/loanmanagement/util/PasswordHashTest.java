package com.loanmanagement.util;

public class PasswordHashTest {

    public static void main(String[] args) {

        String password = "customer123";

        String hash = PasswordUtil.hashPassword(password);

        System.out.println("Password: " + password);
        System.out.println("Hash: " + hash);
    }
}