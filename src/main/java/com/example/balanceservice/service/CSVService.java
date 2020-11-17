package com.example.balanceservice.service;

import com.example.balanceservice.exception.model.CSVParseException;
import com.example.balanceservice.exception.model.IllegalCSVArgumentException;
import com.example.balanceservice.exception.model.InvalidCSVHeaderException;
import com.example.balanceservice.exception.model.UnsupportedFileTypeException;
import com.example.balanceservice.helper.CSVHelper;
import com.example.balanceservice.model.BankStatement;
import org.apache.commons.csv.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CSVService {
    // TODO: Maybe define fields in a object which would contain isMandatory value?
    // TODO: Figure out how to map headers to BankStatement object, so they could be retrieved as parameters.
    private static final List<String> CSV_HEADERS = new ArrayList<>(
            Arrays.asList(
                    "AccountNumber",
                    "Date",
                    "Beneficiary",
                    "Comment",
                    "Amount",
                    "Currency"
            )
    );

    public final String TYPE = "text/csv";
    private BankStatementValidationService bankStatementValidationService;

    @Autowired
    CSVService(BankStatementValidationService bankStatementValidationService) {
        this.bankStatementValidationService = bankStatementValidationService;
    }

    public boolean hasCSVFormat(MultipartFile file) {
        return TYPE.equals(file.getContentType());
    }

    public InputStreamResource bankStatementsToCSV(List<BankStatement> bankStatements) {
        final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format);) {

            csvPrinter.printRecord(CSV_HEADERS);

            for (BankStatement bankStatement : bankStatements) {
                List<String> data = Arrays.asList(
                        bankStatement.getAccountNumber(),
                        bankStatement.getLocalDateTime().toString(),
                        bankStatement.getBeneficiary(),
                        bankStatement.getComment(),
                        bankStatement.getAmount().toString(),
                        bankStatement.getCurrency()
                );

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
        if (!CSVHelper.hasCSVFormat(file)) {
            throw new UnsupportedFileTypeException(file.getContentType());
        }
        try (InputStream is = file.getInputStream();
             BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withTrim());
        ) {
            if (!areListsEqual(csvParser.getHeaderNames(), CSV_HEADERS)) {
                throw new InvalidCSVHeaderException(file.getOriginalFilename(), CSV_HEADERS.toString());
            }

            return csvParser
                    .getRecords()
                    .stream()
                    .map(this::mapCSVRowToBankStatement)
                    .collect(Collectors.toList());

        } catch (IOException e) {
            throw new CSVParseException(file.getOriginalFilename(), e);
        } catch (IllegalArgumentException e) {
            throw new IllegalCSVArgumentException(file.getOriginalFilename(), e.toString());
        }
    }

    private boolean areListsEqual(List<String> one, List<String> two) {
        if (one == null || two == null || one.size() != two.size()) {
            return false;
        }

        final List<String> copyOfOne = new ArrayList<>(one);
        final List<String> copyOfTwo = new ArrayList<>(two);

        copyOfOne.sort(String::compareTo);
        copyOfTwo.sort(String::compareTo);
        return copyOfOne.equals(copyOfTwo);
    }

    BankStatement mapCSVRowToBankStatement(CSVRecord row) {
        // TODO: Somehow map headers so to BankStatement object, so indexes are not hardcoded..
        BankStatement bankStatement = new BankStatement.Builder(
                row.get(CSV_HEADERS.get(0)),
                LocalDateTime.parse(row.get(CSV_HEADERS.get(1))),
                row.get(CSV_HEADERS.get(2)),
                new BigDecimal(row.get(CSV_HEADERS.get(4))),
                row.get(CSV_HEADERS.get(5))
        ).withComment(CSV_HEADERS.get(3))
                .build();

        if (!bankStatementValidationService.isBankStatementValid(bankStatement)) {
            throw new IllegalCSVArgumentException();
        }
        return bankStatement;
    }

}
