package com.example.balanceservice.repository.currencies;

import java.math.BigDecimal;

public interface CurrenciesRepository {
    BigDecimal getRate(String sourceCurrency, String destinationCurrency);
}
