package com.example.balanceservice.dao.currencies;

import java.math.BigDecimal;

public interface CurrenciesRepository {
    BigDecimal getRate(String sourceCurrency, String destinationCurrency);
}
