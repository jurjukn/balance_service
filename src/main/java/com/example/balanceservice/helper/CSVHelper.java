package com.example.balanceservice.helper;

import com.example.balanceservice.exception.model.CSVParseException;
import com.example.balanceservice.exception.model.IllegalCSVArgumentException;
import com.example.balanceservice.exception.model.InvalidCSVHeaderException;
import com.example.balanceservice.exception.model.UnsupportedFileTypeException;
import com.example.balanceservice.model.BankStatement;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CSVHelper {
    // TODO: Maybe define fields in a object which would contain isMandatory value?
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
    public static String TYPE = "text/csv";

    public static boolean hasCSVFormat(MultipartFile file) {
        return TYPE.equals(file.getContentType());
    }

    public static InputStreamResource bankStatementsToCSV(List<BankStatement> bankStatements) {
        final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format);) {

            csvPrinter.printRecord(CSV_HEADERS);

            for (BankStatement bankStatement : bankStatements) {
                List<String> data = Arrays.asList(
                        bankStatement.getAccountNumber(),
                        bankStatement.getDateTime().toString(),
                        bankStatement.getBeneficiary(),
                        bankStatement.getComment(),
                        bankStatement.getAmount().toString(),
                        bankStatement.getCurrency()
                );

                csvPrinter.printRecord(data);
            }

            csvPrinter.flush();
            final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(out.toByteArray());
            final InputStreamResource inputStreamResource = new InputStreamResource(byteArrayInputStream);
            return inputStreamResource;
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to CSV file: " + e.getMessage());
        }
    }

    public static List<BankStatement> csvToBankStatements(MultipartFile file) {
        if (!CSVHelper.hasCSVFormat(file)) {
            throw new UnsupportedFileTypeException(file.getContentType());
        }
        try (InputStream is = file.getInputStream();
             BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());
        ) {
            if (!csvParser.getHeaderNames().equals(CSVHelper.CSV_HEADERS)) {
                throw new InvalidCSVHeaderException(file.getOriginalFilename(), CSVHelper.CSV_HEADERS.toString());
            }

            List<BankStatement> bankStatements = new ArrayList<>();
            csvParser.forEach(csvRecord -> {
                final BankStatement bankStatement = new BankStatement(csvRecord);
                if (!bankStatement.isValid()) {
                    throw new IllegalCSVArgumentException(file.getOriginalFilename());
                }
                bankStatements.add(bankStatement);
            });
            return bankStatements;
        } catch (IOException e) {
            throw new CSVParseException(file.getOriginalFilename(), e);
        } catch (IllegalArgumentException e) {
            throw new IllegalCSVArgumentException(file.getOriginalFilename(), e.toString());
        }
    }
}
