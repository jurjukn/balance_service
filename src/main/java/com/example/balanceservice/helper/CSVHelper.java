package com.example.balanceservice.helper;

import com.example.balanceservice.exception.model.CSVParseException;
import com.example.balanceservice.exception.model.InvalidCSVHeaderException;
import com.example.balanceservice.exception.model.UnsupportedFileTypeException;
import com.example.balanceservice.model.BankStatement;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CSVHelper {
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
            csvParser.forEach(csvRecord -> bankStatements.add(new BankStatement(csvRecord)));
            return bankStatements;
        } catch (IOException e) {
            throw new CSVParseException(file.getOriginalFilename(), e);
        }
    }
}
