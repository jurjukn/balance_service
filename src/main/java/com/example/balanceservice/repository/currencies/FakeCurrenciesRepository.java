package com.example.balanceservice.repository.currencies;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.balanceservice.exception.model.UnsupportedCurrencyException;
import com.example.balanceservice.model.Currency;

@Repository("fakeCurrenciesRepository")
public class FakeCurrenciesRepository implements CurrenciesRepository {

    private static final List<Currency> DB = new ArrayList<>();

    public FakeCurrenciesRepository() {
        // This is hardcoded and should be removed before production.
        Currency usd = new Currency("USD");
        usd.setExchangeRate("EUR", new BigDecimal("0.88"));
        Currency eur = new Currency("EUR");
        eur.setExchangeRate("USD", new BigDecimal("1.13"));
        DB.add(usd);
        DB.add(eur);
    }

    @Override
    public BigDecimal getRate(String sourceCurrency, String destinationCurrency) {
        Optional<Currency> source = DB.stream().filter(currency -> currency.getSymbol().equals(sourceCurrency)).findFirst();
        if (!source.isPresent()) {
            throw new UnsupportedCurrencyException(sourceCurrency);
        }

        return source.get().getExchangeRates().get(destinationCurrency);
    }
}
