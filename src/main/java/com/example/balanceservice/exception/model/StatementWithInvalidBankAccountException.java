package com.example.balanceservice.exception.model;

public class StatementWithInvalidBankAccountException extends RuntimeException {
    public StatementWithInvalidBankAccountException(String statementSource) {
        super("File " + statementSource + " contains invalid bank account number.");
    }
}
