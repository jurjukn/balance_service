package com.example.balanceservice.exception.model;

public class BankAccountNotFoundException extends RuntimeException {
    public BankAccountNotFoundException(String accountNumber) {
        super("AccountNumber: " + accountNumber + " not found.");
    }
}
