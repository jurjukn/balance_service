package com.example.balanceservice.dao.bank_accounts;

import com.example.balanceservice.dto.DataFilterDTO;
import com.example.balanceservice.exception.model.BankAccountNotFoundException;
import com.example.balanceservice.model.BankAccount;
import com.example.balanceservice.model.BankStatement;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository("fakeBankAccountsDao")
public class FakeBankAccountsDataAccessService implements BankAccountsRepository {

    private static HashMap<String, BankAccount> DB = new HashMap<>();

    public FakeBankAccountsDataAccessService() {
        // This is hardcoded and should be removed before production.
        DB.put("123", new BankAccount("123"));
        DB.put("222", new BankAccount("222"));

    }

    @Override
    public void importBankStatement(BankStatement bankStatement) {
        this.getBankAccount(bankStatement.getAccountNumber()).addBankStatement(bankStatement);
    }

    @Override
    public BankAccount getBankAccount(String accountNumber) {
        if (!DB.containsKey(accountNumber)) {
            throw new BankAccountNotFoundException(accountNumber);
        }
        return DB.get(accountNumber);
    }

    @Override
    public List<BankStatement> getBankAccountStatements(String accountNumber) {
        return new ArrayList<>(getBankAccount(accountNumber).getBankStatements());
    }

    @Override
    public List<String> getBankAccountsNumbers() {
        return new ArrayList<>(DB.keySet());
    }

    @Override
    public List<BankStatement> filterBankStatements(DataFilterDTO filter) {
        List<BankStatement> bankStatements = new ArrayList<>();
        for (BankAccount value : DB.values()) {
            bankStatements.addAll(value.getBankStatements());
        }

        return bankStatements
                .stream()
                .filter(statement -> isAfter(statement, filter.getDateFrom()))
                .filter(statement -> isBefore(statement, filter.getDateTo()))
                .collect(Collectors.toList());
    }

    @Override
    public List<BankStatement> filterBankAccountStatements(String accountNumber, DataFilterDTO filter) {
        return this.getBankAccountStatements(accountNumber)
                .stream()
                .filter(statement -> isAfter(statement, filter.getDateFrom()))
                .filter(statement -> isBefore(statement, filter.getDateTo()))
                .collect(Collectors.toList());
    }

    private boolean isAfter(BankStatement statement, LocalDate dateFrom) {
        return Optional.ofNullable(dateFrom)
                .map(date -> {
                    final LocalDate statementDate = statement.getDateTime().toLocalDate();
                    return statementDate.isAfter(dateFrom) || statement.equals(dateFrom);
                })
                .orElse(true);
    }

    private boolean isBefore(BankStatement statement, LocalDate dateTo) {
        return Optional.ofNullable(dateTo)
                .map(date -> {
                    final LocalDate statementDate = statement.getDateTime().toLocalDate();
                    return statementDate.isBefore(dateTo) || statement.equals(dateTo);
                })
                .orElse(true);
    }
}
