package com.example.balanceservice.service;

import com.example.balanceservice.dao.bank_accounts.BankAccountsRepository;
import com.example.balanceservice.dto.DataFilterDTO;
import com.example.balanceservice.exception.model.BankAccountNotFoundException;
import com.example.balanceservice.exception.model.StatementWithInvalidBankAccountException;
import com.example.balanceservice.helper.CSVHelper;
import com.example.balanceservice.model.BankStatement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class BankService {
    private final BankAccountsRepository bankAccountsRepository;
    private final CurrencyService currencyService;

    @Autowired
    public BankService(
            @Qualifier("fakeBankAccountsDao") BankAccountsRepository bankAccountsRepository,
            CurrencyService currencyService) {
        this.bankAccountsRepository = bankAccountsRepository;
        this.currencyService = currencyService;
    }

    public String calculateBalance(String accountNumber, String resultCurrency,
                                   DataFilterDTO dataFilterDTO) {

        final List<BankStatement> statements = bankAccountsRepository
                .filterBankAccountStatements(accountNumber, dataFilterDTO);

        BigDecimal balance = BigDecimal.ZERO;

        for (BankStatement statement : statements) {
            BigDecimal amount = statement.getAmount();
            if (!statement.getCurrency().equals(resultCurrency)) {
                amount = currencyService.convert(
                        statement.getAmount(),
                        statement.getCurrency(),
                        resultCurrency
                );
            }
            balance = balance.add(amount);
        }

        return balance.toString() + " " + resultCurrency;
    }

    public void importBankAccountStatements(String accountNumber, MultipartFile statementsFile) {
        if (!bankAccountsRepository.getBankAccounts().containsKey(accountNumber)) {
            throw new BankAccountNotFoundException(accountNumber);
        }
        final List<BankStatement> bankStatements = CSVHelper.csvToBankStatements(statementsFile);

        final Optional<BankStatement> invalidStatement = bankStatements.stream().filter(
                bankStatement -> !bankStatement.getAccountNumber().equals(accountNumber)
        ).findFirst();

        if (invalidStatement.isPresent()) {
            throw new StatementWithInvalidBankAccountException(statementsFile.getOriginalFilename(),
                    invalidStatement.get().getAccountNumber());
        }

        bankStatements.forEach(bankAccountsRepository::importBankStatement);
    }

    public void importStatements(MultipartFile statementsFile) {

        final List<BankStatement> bankStatements = CSVHelper.csvToBankStatements(statementsFile);

        final Optional<BankStatement> invalidStatement = bankStatements.stream().filter(
                bankStatement ->
                        !bankAccountsRepository.getBankAccounts().containsKey(bankStatement.getAccountNumber())
        ).findFirst();

        if (invalidStatement.isPresent()) {
            throw new StatementWithInvalidBankAccountException(statementsFile.getOriginalFilename(),
                    invalidStatement.get().getAccountNumber());
        }

        bankStatements.forEach(bankAccountsRepository::importBankStatement);
    }

    public InputStreamResource exportBankAccountStatements(String accountNumber, DataFilterDTO dataFilterDTO) {
        List<BankStatement> bankStatements = bankAccountsRepository
                .filterBankAccountStatements(accountNumber, dataFilterDTO);

        return CSVHelper.bankStatementsToCSV(bankStatements);
    }

    public InputStreamResource exportStatements(DataFilterDTO filterDTO) {
        List<BankStatement> bankStatements = bankAccountsRepository.filterBankStatements(filterDTO);
        return CSVHelper.bankStatementsToCSV(bankStatements);
    }
}
