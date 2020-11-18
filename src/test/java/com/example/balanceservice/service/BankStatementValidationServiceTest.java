package com.example.balanceservice.service;

import com.example.balanceservice.model.BankStatement;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


class BankStatementValidationServiceTest {

    static BankStatementValidationService bankStatementValidationService;
    static List<BankStatement> statementList;
    static List<String> accountNumbers;

    @BeforeAll
    public static void setup() {
        bankStatementValidationService = new BankStatementValidationService();

        accountNumbers = new ArrayList<>(Arrays.asList(
                "123",
                "222"
        ));

        statementList = new ArrayList<>();

        BankStatement statement1 = new BankStatement(accountNumbers.get(0),
                LocalDateTime.parse("2012-07-14T17:45:55.9483536"),
                "John Smith",
                "bill",
                new BigDecimal("13.5"),
                "USD");

        BankStatement statement2 = new BankStatement(accountNumbers.get(0),
                LocalDateTime.parse("2014-07-14T17:45:55.9483536"),
                "Jack Samuel",
                "debt",
                new BigDecimal("-13.5"),
                "USD");

        BankStatement statement3 = new BankStatement(accountNumbers.get(1),
                LocalDateTime.parse("2018-07-14T17:45:55.9483536"),
                "Megan Smith",
                "transaction",
                new BigDecimal("9.5"),
                "USD");

        BankStatement statement4 = new BankStatement(accountNumbers.get(1),
                LocalDateTime.parse("2013-07-14T17:45:55.9483536"),
                "Astrid Smith",
                "transaction",
                new BigDecimal("2.5"),
                "USD");

        statementList.add(statement1);
        statementList.add(statement2);
        statementList.add(statement3);
        statementList.add(statement4);

    }

    @Test
    void bankStatementsMatchBankAccount() {
        boolean isMatching1 =
                bankStatementValidationService.bankStatementsMatchBankAccount(statementList, accountNumbers.get(0));
        assertEquals(false, isMatching1);
    }

    @Test
    void bankStatementsMatchBankAccounts() {
        boolean isMatching1 =
                bankStatementValidationService.bankStatementsMatchBankAccounts(statementList, accountNumbers);
        assertEquals(true, isMatching1);

        BankStatement invalidStatement = new BankStatement("55555",
                LocalDateTime.parse("2013-07-14T17:45:55.9483536"),
                "Astrid Smith",
                "transaction",
                new BigDecimal("2.5"),
                "USD");

        statementList.add(invalidStatement);

        boolean isMatching2 =
                bankStatementValidationService.bankStatementsMatchBankAccounts(statementList, accountNumbers);

        assertEquals(false, isMatching2);
    }

    @Test
    void isBankStatementValid() {
        BankStatement invalidStatement1 = new BankStatement("",
                LocalDateTime.parse("2013-07-14T17:45:55.9483536"),
                "Astrid Smith",
                "transaction",
                new BigDecimal("2.5"),
                "USD");
        assertEquals(false, bankStatementValidationService.isBankStatementValid(invalidStatement1));

        BankStatement invalidStatement2 = new BankStatement("123",
                null,
                "Astrid Smith",
                "transaction",
                new BigDecimal("2.5"),
                "USD");
        assertEquals(false, bankStatementValidationService.isBankStatementValid(invalidStatement2));
    }
}