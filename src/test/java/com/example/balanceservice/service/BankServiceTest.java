package com.example.balanceservice.service;

import com.example.balanceservice.dao.bank_accounts.FakeBankAccountsDataAccessService;
import com.example.balanceservice.dao.currencies.FakeCurrenciesDataAccessService;
import com.example.balanceservice.dto.DataFilterDTO;
import com.example.balanceservice.model.BankAccount;
import com.example.balanceservice.model.BankStatement;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BankServiceTest {

    @Test
    void calculateBalance() {
        final FakeBankAccountsDataAccessService fakeBankAccountsDataAccessService
                = new FakeBankAccountsDataAccessService();
        final FakeCurrenciesDataAccessService fakeCurrenciesDataAccessService = new FakeCurrenciesDataAccessService();
        final CurrencyService currencyService = new CurrencyService(fakeCurrenciesDataAccessService);
        final BankStatementValidationService bankStatementValidationService = new BankStatementValidationService();

        final BankService bankService = new BankService(fakeBankAccountsDataAccessService,
                currencyService, bankStatementValidationService);

        String bankAccountNumber = "123";

        BankStatement statement1 = new BankStatement(bankAccountNumber,
                LocalDateTime.parse("2012-07-14T17:45:55.9483536"),
                "John Smith",
                "bill",
                new BigDecimal("13.5"),
                "USD");

        BankStatement statement2 = new BankStatement(bankAccountNumber,
                LocalDateTime.parse("2014-07-14T17:45:55.9483536"),
                "Jack Samuel",
                "debt",
                new BigDecimal("-13.5"),
                "USD");

        BankStatement statement3 = new BankStatement(bankAccountNumber,
                LocalDateTime.parse("2018-07-14T17:45:55.9483536"),
                "Megan Smith",
                "transaction",
                new BigDecimal("9.5"),
                "EUR");

        final BankAccount account1 = fakeBankAccountsDataAccessService.getBankAccount(bankAccountNumber);
        account1.addBankStatement(statement1);
        account1.addBankStatement(statement2);
        account1.addBankStatement(statement3);
        DataFilterDTO filterDTO1 = new DataFilterDTO(LocalDate.parse("2001-01-01"), null);
        String balance = bankService.calculateBalance(bankAccountNumber, "EUR", filterDTO1);

        assertEquals("9.50 EUR", balance);
    }
}
