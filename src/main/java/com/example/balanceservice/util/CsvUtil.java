package com.example.balanceservice.util;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

import com.example.balanceservice.model.BankStatement;

final public class CsvUtil {
    public static final String OUTPUT_FILE_NAME = "statements.csv";
    public static final List<String> CSV_HEADERS = new ArrayList<>(Arrays.asList("AccountNumber", "Date", "Beneficiary", "Comment", "Amount", "Currency"));
    private static final String TYPE = "text/csv";

    public static boolean hasCsvContentType(MultipartFile file) {
        return TYPE.equals(file.getContentType());
    }

    public static boolean hasValidHeaders(List<String> headers, List<String> validHeaders) {
        if (headers == null || validHeaders == null || headers.size() != validHeaders.size()) {
            return false;
        }

        final List<String> copyOfOne = new ArrayList<>(headers);
        final List<String> copyOfTwo = new ArrayList<>(validHeaders);

        copyOfOne.sort(String::compareTo);
        copyOfTwo.sort(String::compareTo);
        return copyOfOne.equals(copyOfTwo);
    }

    public static BankStatement mapCsvRowToBankStatement(CSVRecord row) {
        // TODO: Somehow map headers so to BankStatement object, so indexes are not hardcoded
        return new BankStatement.Builder(row.get(CSV_HEADERS.get(0)), LocalDateTime.parse(row.get(CSV_HEADERS.get(1))), row.get(CSV_HEADERS.get(2)), new BigDecimal(row.get(CSV_HEADERS.get(4))),
                row.get(CSV_HEADERS.get(5))).withComment(CSV_HEADERS.get(3)).build();
    }
}
