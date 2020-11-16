package com.example.balanceservice.dao.bank_accounts;

import com.example.balanceservice.dto.DataFilterDTO;
import com.example.balanceservice.model.BankAccount;
import com.example.balanceservice.model.BankStatement;

import java.util.HashMap;
import java.util.List;

public interface BankAccountsRepository {

    void importBankStatement(BankStatement bankStatements);

    BankAccount getBankAccount(String accountNumber);

    List<BankStatement> getBankAccountStatements(String accountNumber);

    HashMap<String, BankAccount> getBankAccounts();

    List<BankStatement> filterBankAccountStatements(String accountNumber, DataFilterDTO filter);

    List<BankStatement> filterBankStatements(DataFilterDTO filter);
}
