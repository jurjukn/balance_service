package com.example.balanceservice.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.balanceservice.dto.BalanceDTO;
import com.example.balanceservice.dto.DataFilterDTO;
import com.example.balanceservice.exception.model.StatementWithInvalidBankAccountException;
import com.example.balanceservice.model.BankAccount;
import com.example.balanceservice.model.BankStatement;
import com.example.balanceservice.repository.bank.BankRepository;

@Service
public class BankService {

    private final BankRepository bankRepository;
    private final CurrencyService currencyService;
    private final BankStatementValidationService bankStatementValidationService;
    private final CSVService csvService;

    public BankService(@Qualifier("fakeBankRepository") BankRepository bankRepository, CurrencyService currencyService, BankStatementValidationService bankStatementValidationService,
            CSVService csvService) {

        this.bankRepository = bankRepository;
        this.currencyService = currencyService;
        this.bankStatementValidationService = bankStatementValidationService;
        this.csvService = csvService;

    }

    public BalanceDTO calculateBalance(String accountNumber, String resultCurrency, DataFilterDTO dataFilterDTO) {

        final List<BankStatement> statements = bankRepository.filterBankAccountStatements(accountNumber, dataFilterDTO);

        BigDecimal balance = BigDecimal.ZERO;

        for (BankStatement statement : statements) {
            BigDecimal amount = statement.getAmount();
            if (!statement.getCurrency().equals(resultCurrency)) {
                amount = currencyService.convert(statement.getAmount(), statement.getCurrency(), resultCurrency);
            }
            balance = balance.add(amount);
        }

        return new BalanceDTO(balance, resultCurrency);
    }

    public void importBankAccountStatements(String accountNumber, MultipartFile statementsFile) {
        final BankAccount bankAccount = bankRepository.getBankAccount(accountNumber);
        final List<BankStatement> bankStatements = csvService.parseBankStatements(statementsFile);

        if (!bankStatementValidationService.bankStatementsMatchBankAccount(bankStatements, bankAccount.getAccountNumber())) {
            throw new StatementWithInvalidBankAccountException(statementsFile.getOriginalFilename());
        }

        bankStatements.forEach(bankRepository::importBankStatement);
    }

    public InputStreamResource exportBankAccountStatements(String accountNumber, DataFilterDTO dataFilterDTO) {
        List<BankStatement> bankStatements = bankRepository.filterBankAccountStatements(accountNumber, dataFilterDTO);

        return csvService.bankStatementsToCsv(bankStatements);
    }
}
