package com.example.balanceservice.exception.model;

public class CSVParseException extends RuntimeException {
    public CSVParseException(String filename, Throwable throwable) {
        super("Failed to parse csv file: " + filename, throwable);
    }
}
