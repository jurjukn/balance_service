package com.example.balanceservice.api;

import com.example.balanceservice.dto.DataFilterDTO;
import com.example.balanceservice.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class BankAccountController {
    private final BankService bankService;

    @Autowired
    public BankAccountController(BankService bankService) {
        this.bankService = bankService;
    }

    // Get bank account balance.
    @RequestMapping("api/bank_accounts/{account_number}/balance")
    @GetMapping
    public String getBankAccountBalance(@PathVariable("account_number") String accountNumber,
                                        DataFilterDTO dataFilterDTO) {

        // Currency is hardcoded as no information about the currency of
        // balance is provided in the assignment.
        return bankService.calculateBalance(accountNumber, "EUR", dataFilterDTO);
    }

    // Import bank statement for bank account.
    @RequestMapping("api/bank_accounts/{account_number}/bank_statements/import")
    @PostMapping
    public ResponseEntity importStatement(@PathVariable("account_number") String accountNumber,
                                          @RequestParam("file") MultipartFile file) {
        bankService.importBankAccountStatements(accountNumber, file);
        return ResponseEntity.status(HttpStatus.OK).body("Bank account successfully updated with statements");
    }

    // Import bank statement for multiple bank accounts.
    @RequestMapping("api/bank_statements/import")
    @PostMapping
    public ResponseEntity importStatements(@RequestParam("file") MultipartFile file) {
        bankService.importStatements(file);
        return ResponseEntity.status(HttpStatus.OK).body("Statements successfully imported.");
    }

    // Export bank statements for bank account.
    @RequestMapping("api/bank_accounts/{account_number}/bank_statements/export")
    @GetMapping
    public ResponseEntity<Resource> exportStatements(@PathVariable("account_number") String accountNumber,
                                                     DataFilterDTO dataFilterDTO) {

        String fileName = "statements.csv";
        InputStreamResource file = bankService.exportBankAccountStatements(accountNumber, dataFilterDTO);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.parseMediaType("application/csv"))
                .body(file);
    }

    // Export bank statements.
    @RequestMapping("api/bank_statements/export")
    @GetMapping
    public ResponseEntity<Resource> exportStatements(DataFilterDTO dataFilterDTO) {

        String fileName = "statements.csv";
        InputStreamResource file = bankService.exportStatements(dataFilterDTO);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.parseMediaType("application/csv"))
                .body(file);
    }
}
