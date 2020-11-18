package com.example.balanceservice.service;

import com.example.balanceservice.dto.BalanceDTO;
import com.example.balanceservice.repository.bank.FakeBankRepository;
import com.example.balanceservice.repository.currencies.FakeCurrenciesRepository;
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
        final FakeBankRepository fakeBankAccountsRepository
                = new FakeBankRepository();
        final FakeCurrenciesRepository fakeCurrenciesRepository = new FakeCurrenciesRepository();
        final CurrencyService currencyService = new CurrencyService(fakeCurrenciesRepository);
        final BankStatementValidationService bankStatementValidationService = new BankStatementValidationService();
        final CSVService csvService = new CSVService(bankStatementValidationService);

        final BankService bankService = new BankService(fakeBankAccountsRepository,
                currencyService, bankStatementValidationService, csvService);

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

        final BankAccount account1 = fakeBankAccountsRepository.getBankAccount(bankAccountNumber);
        account1.addBankStatement(statement1);
        account1.addBankStatement(statement2);
        account1.addBankStatement(statement3);
        DataFilterDTO filterDTO1 = new DataFilterDTO(LocalDate.parse("2001-01-01"), null);
        BalanceDTO balance = bankService.calculateBalance(bankAccountNumber, "EUR", filterDTO1);

        assertEquals("9.50", balance.getAmount().toString());
        assertEquals("EUR", balance.getCurrency());
    }
}
