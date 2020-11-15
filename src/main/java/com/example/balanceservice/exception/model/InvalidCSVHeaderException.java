package com.example.balanceservice.exception.model;

public class InvalidCSVHeaderException extends RuntimeException {
    public InvalidCSVHeaderException(String fileName, String validHeaderFormat) {
        super("File " + fileName + " has invalid headers. Headers must be: " + validHeaderFormat);
    }
}
