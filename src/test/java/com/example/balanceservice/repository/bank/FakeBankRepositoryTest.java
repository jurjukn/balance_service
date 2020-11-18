package com.example.balanceservice.repository.bank;

import com.example.balanceservice.dto.DataFilterDTO;
import com.example.balanceservice.model.BankAccount;
import com.example.balanceservice.model.BankStatement;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FakeBankRepositoryTest {

    static FakeBankRepository fakeBankAccountsRepository;
    static String bankAccountNumber1;
    static String bankAccountNumber2;

    @BeforeAll
    public static void setup() {
        fakeBankAccountsRepository = new FakeBankRepository();
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
    void filterBankStatements() {
        DataFilterDTO filterDTO = new DataFilterDTO(LocalDate.parse("2014-01-01"), null);
        final List<BankStatement> filteredStatementList = fakeBankAccountsRepository
                .filterBankStatements(filterDTO);
        assertEquals(4, filteredStatementList.size());
    }

    @Test
    void filterBankAccountStatements() {
        DataFilterDTO filterDTO = new DataFilterDTO(null, LocalDate.parse("2017-01-01"));
        final List<BankStatement> filteredStatementList = fakeBankAccountsRepository
                .filterBankAccountStatements(bankAccountNumber1, filterDTO);
        assertEquals(2, filteredStatementList.size());
    }
}
