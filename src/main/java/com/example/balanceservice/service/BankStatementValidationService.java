package com.example.balanceservice.service;

import com.example.balanceservice.model.BankStatement;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankStatementValidationService {

    boolean bankStatementsMatchBankAccount(List<BankStatement> bankStatements, String accountNumber) {
        return bankStatements
                .stream()
                .allMatch(bankStatement -> bankStatement.getAccountNumber().equals(accountNumber));
    }

    boolean bankStatementsMatchBankAccounts(List<BankStatement> bankStatements,
                                            List<String> accountNumbers) {
        return bankStatements
                .stream()
                .allMatch(statement -> accountNumbers.contains(statement.getAccountNumber()));
    }

}
