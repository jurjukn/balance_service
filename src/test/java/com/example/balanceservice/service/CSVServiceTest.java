package com.example.balanceservice.service;

import com.example.balanceservice.exception.model.InvalidCSVHeaderException;
import com.example.balanceservice.exception.model.UnsupportedFileTypeException;
import com.example.balanceservice.model.BankStatement;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.InputStreamResource;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CSVServiceTest {
    static BankStatementValidationService bankStatementValidationService;
    static CSVService csvService;

    @BeforeAll
    public static void setup() {
        bankStatementValidationService = new BankStatementValidationService();
        csvService = new CSVService(bankStatementValidationService);

    }

    @Test
    void testFileValidation() {
        MockMultipartFile txtFile = new MockMultipartFile(
                "data1", "filename1.txt", "text/plain", "some xml".getBytes());
        assertThrows(UnsupportedFileTypeException.class, () -> csvService.parseBankStatements(txtFile));

        MockMultipartFile htmlFile = new MockMultipartFile(
                "data2", "filename2.html", "text/html", "some html".getBytes());
        assertThrows(UnsupportedFileTypeException.class, () -> csvService.parseBankStatements(htmlFile));

        MockMultipartFile xlsFile = new MockMultipartFile(
                "data3", "filename3.xls", "application/vnd.ms-excel", "some csv".getBytes());
        assertThrows(UnsupportedFileTypeException.class, () -> csvService.parseBankStatements(xlsFile));

        MockMultipartFile csvFile1 = new MockMultipartFile(
                "data4", "filename4.csv",
                "text/csv", "AccountNumber,Date,Beneficiary,Comment,Amount,Currencyaaa".getBytes());
        assertThrows(InvalidCSVHeaderException.class, () -> csvService.parseBankStatements(csvFile1));
    }

    @Test
    void parseBankStatements() {
        MockMultipartFile csvFile2 = new MockMultipartFile(
                "data4", "filename4.csv",
                "text/csv", "AccountNumber,Date,Beneficiary,Comment,Amount,Currency".getBytes());
        assertEquals(0, csvService.parseBankStatements(csvFile2).size());
    }

    @Test
    void bankStatementsToCsv() throws IOException {

        List<BankStatement> bankStatements = new ArrayList<>();
        InputStreamResource inputStreamResource1 = csvService.bankStatementsToCsv(bankStatements);
        assertEquals(56, inputStreamResource1.contentLength());

    }

}