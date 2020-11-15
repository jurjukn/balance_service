package com.example.balanceservice.exception.model;

public class IllegalCSVArgumentException extends RuntimeException {
    public IllegalCSVArgumentException(String filename, String exceptionType) {
        super("File " + filename + " contains invalid format data: " + exceptionType);
    }

    public IllegalCSVArgumentException(String filename) {
        super("File " + filename + " contains invalid format data");
    }
}
