package com.example.balanceservice.model;

import java.math.BigDecimal;
import java.util.HashMap;

public class Currency {
    private final String symbol;
    private final HashMap<String, BigDecimal> exchangeRates = new HashMap<>();

    public Currency(String symbol) {
        this.symbol = symbol;
    }

    public void setExchangeRate(String destinationCurrency, BigDecimal rate) {
        exchangeRates.put(destinationCurrency, rate);
    }

    public String getSymbol() {
        return symbol;
    }

    public HashMap<String, BigDecimal> getExchangeRates() {
        return exchangeRates;
    }
}
