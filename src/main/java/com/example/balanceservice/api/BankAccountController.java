package com.example.balanceservice.api;

import com.example.balanceservice.service.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Optional;

@RestController
public class BankAccountController {
    private final BankAccountService bankAccountService;

    @Autowired
    public BankAccountController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    // Import bank statement for bank account.
    @RequestMapping("api/bank_accounts/{account_number}/bank_statements/import")
    @PostMapping
    public ResponseEntity importStatement(@PathVariable("account_number") String accountNumber,
                                          @RequestParam("file") MultipartFile file) {
        bankAccountService.importBankAccountStatements(accountNumber, file);
        return ResponseEntity.status(HttpStatus.OK).body("Bank account successfully updated with statements");
    }

    // Import bank statement for multiple bank accounts.
    @RequestMapping("api/bank_statements/import")
    @PostMapping
    public ResponseEntity importStatements(@RequestParam("file") MultipartFile file) {
        bankAccountService.importBankStatements(file);
        return ResponseEntity.status(HttpStatus.OK).body("Statements successfully imported.");
    }

    // Export bank statements for bank account.
    @RequestMapping("api/bank_accounts/{account_number}/bank_statements/export")
    @GetMapping
    public ResponseEntity<Resource> exportStatements(@PathVariable("account_number") String accountNumber,
                                                     @RequestParam("dateFrom") Optional<String> dateFrom,
                                                     @RequestParam("dateTo") Optional<String> dateTo) {

        String fileName = "statements.csv";
        InputStreamResource file = bankAccountService.exportBankAccountStatements(accountNumber, dateFrom, dateTo);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.parseMediaType("application/csv"))
                .body(file);
    }
}
