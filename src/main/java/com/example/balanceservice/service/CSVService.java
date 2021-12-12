package com.example.balanceservice.service;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.balanceservice.exception.model.CSVParseException;
import com.example.balanceservice.exception.model.IllegalCSVArgumentException;
import com.example.balanceservice.exception.model.InvalidCSVHeaderException;
import com.example.balanceservice.exception.model.UnsupportedFileTypeException;
import com.example.balanceservice.model.BankStatement;
import com.example.balanceservice.util.CsvUtil;

@Service
public class CSVService {
    private final BankStatementValidationService bankStatementValidationService;

    public CSVService(BankStatementValidationService bankStatementValidationService) {
        this.bankStatementValidationService = bankStatementValidationService;
    }

    public InputStreamResource bankStatementsToCsv(List<BankStatement> bankStatements) {
        final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream(); CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format)) {

            csvPrinter.printRecord(CsvUtil.CSV_HEADERS);

            for (BankStatement bankStatement : bankStatements) {
                List<String> data = Arrays.asList(bankStatement.getAccountNumber(), bankStatement.getLocalDateTime().toString(), bankStatement.getBeneficiary(), bankStatement.getComment(),
                        bankStatement.getAmount().toString(), bankStatement.getCurrency());

                csvPrinter.printRecord(data);
            }

            csvPrinter.flush();
            final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(out.toByteArray());
            return new InputStreamResource(byteArrayInputStream);
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to CSV file: " + e.getMessage());
        }
    }

    public List<BankStatement> parseBankStatements(MultipartFile file) {
        if (!CsvUtil.hasCsvContentType(file)) {
            throw new UnsupportedFileTypeException(file.getContentType());
        }
        try (InputStream is = file.getInputStream(); BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8)); CSVParser csvParser = new CSVParser(fileReader,
                CSVFormat.DEFAULT.withFirstRecordAsHeader().withTrim())) {
            if (!CsvUtil.hasValidHeaders(csvParser.getHeaderNames(), CsvUtil.CSV_HEADERS)) {
                throw new InvalidCSVHeaderException(file.getOriginalFilename(), CsvUtil.CSV_HEADERS.toString());
            }

            List<BankStatement> statements = csvParser.getRecords().stream().map(CsvUtil::mapCsvRowToBankStatement).collect(Collectors.toList());
            statements.forEach(this::validateBankStatement);
            return statements;

        } catch (IOException e) {
            throw new CSVParseException(file.getOriginalFilename(), e);
        } catch (IllegalArgumentException e) {
            throw new IllegalCSVArgumentException(file.getOriginalFilename(), e.toString());
        }
    }

    private BankStatement validateBankStatement(BankStatement statement) {
        if (!bankStatementValidationService.isBankStatementValid(statement)) {
            throw new IllegalCSVArgumentException();
        }
        return statement;
    }
}
