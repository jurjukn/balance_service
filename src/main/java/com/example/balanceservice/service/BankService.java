package com.example.balanceservice.service;

import com.example.balanceservice.dao.bank_accounts.BankAccountsRepository;
import com.example.balanceservice.dto.DataFilterDTO;
import com.example.balanceservice.exception.model.StatementWithInvalidBankAccountException;
import com.example.balanceservice.helper.CSVHelper;
import com.example.balanceservice.model.BankAccount;
import com.example.balanceservice.model.BankStatement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BankService {
    private final BankAccountsRepository bankAccountsRepository;
    private final CurrencyService currencyService;
    private final BankStatementValidationService bankStatementValidationService;

    @Autowired
    public BankService(
            @Qualifier("fakeBankAccountsDao") BankAccountsRepository bankAccountsRepository,
            CurrencyService currencyService,
            BankStatementValidationService bankStatementValidationService) {
        this.bankAccountsRepository = bankAccountsRepository;
        this.currencyService = currencyService;
        this.bankStatementValidationService = bankStatementValidationService;
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
        final BankAccount bankAccount = bankAccountsRepository.getBankAccount(accountNumber);
        final List<BankStatement> bankStatements = CSVHelper.csvToBankStatements(statementsFile);

        if (!bankStatementValidationService.bankStatementsMatchBankAccount(bankStatements,
                bankAccount.getAccountNumber())) {
            throw new StatementWithInvalidBankAccountException(statementsFile.getOriginalFilename());
        }

        bankStatements.forEach(bankAccountsRepository::importBankStatement);
    }

    public void importStatements(MultipartFile statementsFile) {

        final List<BankStatement> bankStatements = CSVHelper.csvToBankStatements(statementsFile);

        if (!bankStatementValidationService.bankStatementsMatchBankAccounts(bankStatements,
                bankAccountsRepository.getBankAccountsNumbers())) {
            throw new StatementWithInvalidBankAccountException(statementsFile
                    .getOriginalFilename());

        }

        bankStatements.forEach(bankAccountsRepository::importBankStatement);
    }

    public InputStreamResource exportBankAccountStatements(String accountNumber, DataFilterDTO dataFilterDTO) {
        List<BankStatement> bankStatements = bankAccountsRepository
                .filterBankAccountStatements(accountNumber, dataFilterDTO);

        return CSVHelper.bankStatementsToCSV(bankStatements);
    }

    public InputStreamResource exportStatements(DataFilterDTO filterDTO) {
        List<BankStatement> bankStatements = bankAccountsRepository
                .filterBankStatements(filterDTO);
        return CSVHelper.bankStatementsToCSV(bankStatements);
    }
}
