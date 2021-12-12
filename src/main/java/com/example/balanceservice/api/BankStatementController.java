package com.example.balanceservice.api;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.balanceservice.dto.DataFilterDTO;
import com.example.balanceservice.service.BankStatementsService;
import com.example.balanceservice.util.CsvUtil;

@RestController
@RequestMapping(value = "/api/bankStatements")
public class BankStatementController {

    private final BankStatementsService bankStatementsService;

    public BankStatementController(BankStatementsService bankStatementsService) {
        this.bankStatementsService = bankStatementsService;
    }

    @PostMapping("/import")
    public ResponseEntity<String> importStatements(@RequestParam("file") MultipartFile file) {
        bankStatementsService.importStatements(file);
        return ResponseEntity.status(HttpStatus.OK).body("Statements successfully imported.");
    }

    @GetMapping("/export")
    public ResponseEntity<Resource> exportStatements(DataFilterDTO dataFilterDTO) {
        String fileName = CsvUtil.OUTPUT_FILE_NAME;
        InputStreamResource file = bankStatementsService.exportStatements(dataFilterDTO);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName).contentType(MediaType.parseMediaType("application/csv")).body(file);
    }
}
