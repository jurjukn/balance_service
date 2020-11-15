package com.example.balanceservice.model;

import java.util.ArrayList;
import java.util.List;

public class BankAccount {
    private String accountNumber;
    private List<BankStatement> bankStatements;

    public BankAccount(String accountNumber) {
        this.accountNumber = accountNumber;
        this.bankStatements = new ArrayList<>();
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public List<BankStatement> getBankStatements() {
        return bankStatements;
    }

    public void addBankStatement(BankStatement statement) {
        bankStatements.add(statement);
    }

    public void addBankStatements(List<BankStatement> statements) {
        bankStatements.addAll(statements);
    }
}
