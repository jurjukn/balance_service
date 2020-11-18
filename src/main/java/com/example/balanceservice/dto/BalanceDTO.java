package com.example.balanceservice.dto;

import java.math.BigDecimal;

public class BalanceDTO {
    BigDecimal amount;
    String currency;

    public BalanceDTO(BigDecimal amount, String currency) {
        this.amount = amount;
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }
}
