package com.example.balanceservice.exception.model;

public class StatementWithInvalidBankAccountException extends RuntimeException {
    public StatementWithInvalidBankAccountException(String statementSource, String accountNumber) {
        super("File " + statementSource + " contains invalid AccountNumber: " + accountNumber);
    }
}
