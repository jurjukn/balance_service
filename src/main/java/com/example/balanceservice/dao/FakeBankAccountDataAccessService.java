package com.example.balanceservice.dao;

import com.example.balanceservice.model.BankAccount;
import com.example.balanceservice.model.BankStatement;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Repository("fakeDao")
public class FakeBankAccountDataAccessService implements BankAccountRepository {

    private static HashMap<String, BankAccount> DB = new HashMap<String, BankAccount>();

    FakeBankAccountDataAccessService() {
        // This is hardcoded and should be removed before production.
        DB.put("123", new BankAccount("123"));
        DB.put("222", new BankAccount("222"));
    }

    @Override
    public void importBankStatement(BankStatement bankStatement) {
        DB.get(bankStatement.getAccountNumber()).addBankStatement(bankStatement);
    }

    @Override
    public int exportBankStatement() {
        return 0;
    }

    @Override
    public HashMap<String, BankAccount> getAllBankAccounts() {
        return DB;
    }
}
