package com.example.balanceservice.api;

import com.example.balanceservice.service.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class BankAccountController {
    private final BankAccountService bankAccountService;

    @Autowired
    public BankAccountController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    // Import bank statement for bank account.
    @RequestMapping("api/bank_accounts/{account_number}/import")
    @PostMapping
    public ResponseEntity importStatement(@PathVariable("account_number") String accountNumber,
                                          @RequestParam("file") MultipartFile file) {
        bankAccountService.importBankAccountStatements(accountNumber, file);
        return ResponseEntity.status(HttpStatus.OK).body("Bank account successfully updated with statements");
    }
}
