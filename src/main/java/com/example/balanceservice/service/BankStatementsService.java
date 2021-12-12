package com.example.balanceservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.balanceservice.dto.DataFilterDTO;
import com.example.balanceservice.exception.model.StatementWithInvalidBankAccountException;
import com.example.balanceservice.model.BankStatement;
import com.example.balanceservice.repository.bank.BankRepository;

@Service
public class BankStatementsService {

    private final BankStatementValidationService bankStatementValidationService;
    private final CSVService csvService;
    private final BankRepository bankRepository;

    public BankStatementsService(BankStatementValidationService bankStatementValidationService, CSVService csvService, @Qualifier("fakeBankRepository") BankRepository bankRepository) {
        this.bankStatementValidationService = bankStatementValidationService;
        this.csvService = csvService;
        this.bankRepository = bankRepository;
    }

    public void importStatements(MultipartFile statementsFile) {

        final List<BankStatement> bankStatements = csvService.parseBankStatements(statementsFile);

        if (!bankStatementValidationService.bankStatementsMatchBankAccounts(bankStatements, bankRepository.getBankAccountsNumbers())) {
            throw new StatementWithInvalidBankAccountException(statementsFile.getOriginalFilename());

        }

        bankStatements.forEach(bankRepository::importBankStatement);
    }

    public InputStreamResource exportStatements(DataFilterDTO filterDTO) {
        List<BankStatement> bankStatements = bankRepository.filterBankStatements(filterDTO);

        return csvService.bankStatementsToCsv(bankStatements);
    }

}
