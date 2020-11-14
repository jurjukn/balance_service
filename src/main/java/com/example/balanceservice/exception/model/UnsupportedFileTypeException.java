package com.example.balanceservice.exception.model;

public class UnsupportedFileTypeException extends RuntimeException {
    public UnsupportedFileTypeException(String fileType) {
        super("File type " + fileType + " is not supported.");
    }
}
