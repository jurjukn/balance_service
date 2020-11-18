package com.example.balanceservice.service;

import com.example.balanceservice.dto.BalanceDTO;
import com.example.balanceservice.dto.DataFilterDTO;
import com.example.balanceservice.model.BankAccount;
import com.example.balanceservice.model.BankStatement;
import com.example.balanceservice.repository.bank.FakeBankRepository;
import com.example.balanceservice.repository.currencies.FakeCurrenciesRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.InputStreamResource;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BankServiceTest {

    static FakeBankRepository fakeBankAccountsRepository;
    static FakeCurrenciesRepository fakeCurrenciesRepository;
    static CurrencyService currencyService;
    static BankStatementValidationService bankStatementValidationService;
    static CSVService csvService;
    static BankService bankService;
    static String bankAccountNumber1;
    static String bankAccountNumber2;

    @BeforeAll
    public static void setup() {
        fakeCurrenciesRepository = new FakeCurrenciesRepository();
        currencyService = new CurrencyService(fakeCurrenciesRepository);
        bankStatementValidationService = new BankStatementValidationService();
        csvService = new CSVService(bankStatementValidationService);
        fakeBankAccountsRepository = new FakeBankRepository();
        bankService = new BankService(fakeBankAccountsRepository,
                currencyService, bankStatementValidationService, csvService);

        bankAccountNumber1 = "123";
        bankAccountNumber2 = "222";

        BankStatement statement1 = new BankStatement(bankAccountNumber1,
                LocalDateTime.parse("2012-07-14T17:45:55.9483536"),
                "John Smith",
                "bill",
                new BigDecimal("13.5"),
                "USD");

        BankStatement statement2 = new BankStatement(bankAccountNumber1,
                LocalDateTime.parse("2014-07-14T17:45:55.9483536"),
                "Jack Samuel",
                "debt",
                new BigDecimal("-13.5"),
                "USD");

        BankStatement statement3 = new BankStatement(bankAccountNumber1,
                LocalDateTime.parse("2018-07-14T17:45:55.9483536"),
                "Megan Smith",
                "transaction",
                new BigDecimal("9.5"),
                "USD");

        BankStatement statement4 = new BankStatement(bankAccountNumber2,
                LocalDateTime.parse("2012-07-14T17:45:55.9483536"),
                "John Smith",
                "bill",
                new BigDecimal("13.5"),
                "USD");

        BankStatement statement5 = new BankStatement(bankAccountNumber2,
                LocalDateTime.parse("2014-07-14T17:45:55.9483536"),
                "Jack Samuel",
                "debt",
                new BigDecimal("-13.5"),
                "USD");

        BankStatement statement6 = new BankStatement(bankAccountNumber2,
                LocalDateTime.parse("2018-07-14T17:45:55.9483536"),
                "Megan Smith",
                "transaction",
                new BigDecimal("9.5"),
                "USD");

        final BankAccount account1 = fakeBankAccountsRepository.getBankAccount("123");
        account1.addBankStatement(statement1);
        account1.addBankStatement(statement2);
        account1.addBankStatement(statement3);


        final BankAccount account2 = fakeBankAccountsRepository.getBankAccount("222");
        account2.addBankStatement(statement4);
        account2.addBankStatement(statement5);
        account2.addBankStatement(statement6);
    }

    @Test
    void calculateBalance() {
        DataFilterDTO filterDTO1 = new DataFilterDTO(null, null);
        BalanceDTO balance1 = bankService.calculateBalance(bankAccountNumber1, "EUR", filterDTO1);
        assertEquals(new BigDecimal("7.98"), balance1.getAmount());
        assertEquals("EUR", balance1.getCurrency());

        DataFilterDTO filterDTO2 = new DataFilterDTO(LocalDate.parse("2015-01-01"), null);
        BalanceDTO balance2 = bankService.calculateBalance(bankAccountNumber2, "USD", filterDTO1);
        assertEquals(new BigDecimal("9.5"), balance2.getAmount());
        assertEquals("USD", balance2.getCurrency());
    }

    @Test
    void exportStatements() throws IOException {
        DataFilterDTO filterDTO1 = new DataFilterDTO(null, null);
        final InputStreamResource allStatements = bankService.exportStatements(filterDTO1);
        final InputStreamResource user1Statements =
                bankService.exportBankAccountStatements(bankAccountNumber1, filterDTO1);

        assertEquals(true, allStatements.contentLength() > user1Statements.contentLength());
    }
}
