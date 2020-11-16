package com.example.balanceservice.exception.model;

public class UnsupportedCurrencyException extends RuntimeException {
    public UnsupportedCurrencyException(String symbol) {
        super("Currency: " + symbol + " is not supported.");
    }
}
