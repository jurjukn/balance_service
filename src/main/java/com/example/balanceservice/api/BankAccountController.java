package com.example.balanceservice.api;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.balanceservice.dto.BalanceDTO;
import com.example.balanceservice.dto.DataFilterDTO;
import com.example.balanceservice.service.BankService;
import com.example.balanceservice.util.CsvUtil;
import com.example.balanceservice.util.CurrencyUtil;

@RestController
@RequestMapping(value = "/api/bankAccounts")
public class BankAccountController {
    private final BankService bankService;

    public BankAccountController(BankService bankService) {
        this.bankService = bankService;
    }

    @GetMapping("/{accountNumber}/balance")
    public BalanceDTO getBankAccountBalance(@PathVariable("accountNumber") String accountNumber, DataFilterDTO dataFilterDTO) {
        return bankService.calculateBalance(accountNumber, CurrencyUtil.DEFAULT_CURRENCY, dataFilterDTO);
    }

    @PostMapping("/{accountNumber}/bankStatements/import")
    public ResponseEntity<String> importStatement(@PathVariable("accountNumber") String accountNumber, @RequestParam("file") MultipartFile file) {
        bankService.importBankAccountStatements(accountNumber, file);
        return ResponseEntity.status(HttpStatus.OK).body("Bank account successfully updated with statements");
    }

    @GetMapping("/{accountNumber}/bankStatements/export")
    public ResponseEntity<Resource> exportStatements(@PathVariable("accountNumber") String accountNumber, DataFilterDTO dataFilterDTO) {
        String fileName = CsvUtil.OUTPUT_FILE_NAME;
        InputStreamResource file = bankService.exportBankAccountStatements(accountNumber, dataFilterDTO);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName).contentType(MediaType.parseMediaType("application/csv")).body(file);
    }
}
