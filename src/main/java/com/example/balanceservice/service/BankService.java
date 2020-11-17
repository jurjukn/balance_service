package com.example.balanceservice.service;

import com.example.balanceservice.dto.DataFilterDTO;
import com.example.balanceservice.exception.model.StatementWithInvalidBankAccountException;
//import com.example.balanceservice.helper.CSVHelper;
import com.example.balanceservice.model.BankAccount;
import com.example.balanceservice.model.BankStatement;
import com.example.balanceservice.repository.bank.BankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BankService {

    private final BankRepository bankRepository;
    private final CurrencyService currencyService;
    private final BankStatementValidationService bankStatementValidationService;
    private final CSVService csvService;

    @Autowired
    public BankService(
            @Qualifier("fakeBankRepository") BankRepository bankRepository,
            CurrencyService currencyService,
            BankStatementValidationService bankStatementValidationService,
            CSVService csvService) {
        try {
            this.bankRepository = bankRepository;
            this.currencyService = currencyService;
            this.bankStatementValidationService = bankStatementValidationService;
            this.csvService = csvService;
        } catch (Exception e){
            int abd = 3;
        }
    }

    public String calculateBalance(String accountNumber, String resultCurrency,
                                   DataFilterDTO dataFilterDTO) {

        final List<BankStatement> statements = bankRepository
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
        final BankAccount bankAccount = bankRepository.getBankAccount(accountNumber);
        final List<BankStatement> bankStatements = csvService.parseBankStatements(statementsFile);

        if (!bankStatementValidationService.bankStatementsMatchBankAccount(bankStatements,
                bankAccount.getAccountNumber())) {
            throw new StatementWithInvalidBankAccountException(statementsFile.getOriginalFilename());
        }

        bankStatements.forEach(bankRepository::importBankStatement);

        int abd = 3;
    }

    public void importStatements(MultipartFile statementsFile) {

        final List<BankStatement> bankStatements = csvService.parseBankStatements(statementsFile);

        if (!bankStatementValidationService.bankStatementsMatchBankAccounts(bankStatements,
                bankRepository.getBankAccountsNumbers())) {
            throw new StatementWithInvalidBankAccountException(statementsFile
                    .getOriginalFilename());

        }

//        bankStatements.forEach(bankRepository::importBankStatement);


        int abd = 3;
    }

    public InputStreamResource exportBankAccountStatements(String accountNumber, DataFilterDTO dataFilterDTO) {
        List<BankStatement> bankStatements = bankRepository
                .filterBankAccountStatements(accountNumber, dataFilterDTO);

        return csvService.bankStatementsToCSV(bankStatements);

//        return CSVHelper.bankStatementsToCSV(bankStatements);
    }

    public InputStreamResource exportStatements(DataFilterDTO filterDTO) {
        List<BankStatement> bankStatements = bankRepository
                .filterBankStatements(filterDTO);

        return csvService.bankStatementsToCSV(bankStatements);
//        return CSVHelper.bankStatementsToCSV(bankStatements);
    }
}
