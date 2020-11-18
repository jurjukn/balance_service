package com.example.balanceservice.repository.bank;

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

@Repository("fakeBankRepository")
public class FakeBankRepository implements BankRepository {

    private static HashMap<String, BankAccount> DB = new HashMap<>();

    public FakeBankRepository() {
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
                .filter(statement -> isStatementAfterDateInclusive(statement, filter.getDateFrom()))
                .filter(statement -> isStatementBeforeDateInclusive(statement, filter.getDateTo()))
                .collect(Collectors.toList());
    }

    @Override
    public List<BankStatement> filterBankAccountStatements(String accountNumber, DataFilterDTO filter) {
        return this.getBankAccountStatements(accountNumber)
                .stream()
                .filter(statement -> isStatementAfterDateInclusive(statement, filter.getDateFrom()))
                .filter(statement -> isStatementBeforeDateInclusive(statement, filter.getDateTo()))
                .collect(Collectors.toList());
    }

    private boolean isStatementAfterDateInclusive(BankStatement statement, LocalDate dateFrom) {
        final LocalDate statementDate = statement.getLocalDateTime().toLocalDate();
        return Optional.ofNullable(dateFrom)
                .map(date -> statementDate.compareTo(dateFrom) >= 0)
                .orElse(true);
    }

    private boolean isStatementBeforeDateInclusive(BankStatement statement, LocalDate dateTo) {
        final LocalDate statementDate = statement.getLocalDateTime().toLocalDate();
        return Optional.ofNullable(dateTo)
                .map(date -> statementDate.compareTo(dateTo) <= 0)
                .orElse(true);
    }
}
