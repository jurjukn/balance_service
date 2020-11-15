package com.example.balanceservice.service;

import com.example.balanceservice.dao.BankAccountRepository;
import com.example.balanceservice.exception.model.BankAccountNotFoundException;
import com.example.balanceservice.exception.model.StatementWithInvalidBankAccountException;
import com.example.balanceservice.helper.CSVHelper;
import com.example.balanceservice.model.BankStatement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BankAccountService {
    private final BankAccountRepository bankAccountRepository;

    @Autowired
    public BankAccountService(@Qualifier("fakeDao") BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    public InputStreamResource exportBankAccountStatements(String accountNumber, Optional<String> dateFrom,
                                                           Optional<String> dateTo) {
        if (!bankAccountRepository.getAllBankAccounts().containsKey(accountNumber)) {
            throw new BankAccountNotFoundException(accountNumber);
        }

        List<BankStatement> bankStatements = bankAccountRepository.getAllBankAccounts()
                .get(accountNumber).getBankStatements();

        if (dateFrom.isPresent()) {
            final LocalDate dateFromEntity = LocalDate.parse(dateFrom.get());
            bankStatements = bankStatements.stream().filter(bankStatement ->
                    {
                        LocalDate bankStatementDate = bankStatement.getDateTime().toLocalDate();
                        return bankStatementDate.isAfter(dateFromEntity)
                                || bankStatementDate.equals(dateFromEntity);
                    }
            ).collect(Collectors.toList());
        }

        if (dateTo.isPresent()) {
            final LocalDate dateToEntity = LocalDate.parse(dateTo.get());
            bankStatements = bankStatements.stream().filter(bankStatement -> {
                final LocalDate statementDateTime = bankStatement.getDateTime().toLocalDate();
                return statementDateTime.isBefore(dateToEntity) || statementDateTime.equals(dateToEntity);
            }).collect(Collectors.toList());
        }

        return CSVHelper.bankStatementsToCSV(bankStatements);
    }

    public void importBankAccountStatements(String accountNumber, MultipartFile statementsFile) {
        if (!bankAccountRepository.getAllBankAccounts().containsKey(accountNumber)) {
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

        bankStatements.forEach(bankAccountRepository::importBankStatement);
    }

    public void importBankStatements(MultipartFile statementsFile) {

        final List<BankStatement> bankStatements = CSVHelper.csvToBankStatements(statementsFile);

        final Optional<BankStatement> invalidStatement = bankStatements.stream().filter(
                bankStatement ->
                        !bankAccountRepository.getAllBankAccounts().containsKey(bankStatement.getAccountNumber())
        ).findFirst();

        if (invalidStatement.isPresent()) {
            throw new StatementWithInvalidBankAccountException(statementsFile.getOriginalFilename(),
                    invalidStatement.get().getAccountNumber());
        }

        bankStatements.forEach(bankAccountRepository::importBankStatement);
    }
}
