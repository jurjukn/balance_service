package com.example.balanceservice.service;

import com.example.balanceservice.repository.currencies.CurrenciesRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class CurrencyService {
    private final CurrenciesRepository currenciesRepository;

    public CurrencyService(@Qualifier("fakeCurrenciesRepository") CurrenciesRepository currenciesRepository) {
        this.currenciesRepository = currenciesRepository;
    }

    BigDecimal convert(BigDecimal amount, String sourceCurrency, String destinationCurrency) {
        BigDecimal rate = currenciesRepository.getRate(sourceCurrency, destinationCurrency);
        return rate.multiply(amount).setScale(2, RoundingMode.HALF_DOWN);
    }
}
