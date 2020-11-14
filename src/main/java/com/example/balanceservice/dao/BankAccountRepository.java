package com.example.balanceservice.dao;

import com.example.balanceservice.model.BankAccount;
import com.example.balanceservice.model.BankStatement;

import java.util.HashMap;

public interface BankAccountRepository {

    void importBankStatement(String accountNumber, BankStatement bankStatements);

    int exportBankStatement();

    HashMap<String, BankAccount> getAllBankAccounts();
}
