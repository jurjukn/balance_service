package com.example.balanceservice.dao.currencies;

import com.example.balanceservice.exception.model.UnsupportedCurrencyException;
import com.example.balanceservice.model.Currency;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("fakeCurrenciesDao")
public class FakeCurrenciesDataAccessService implements CurrenciesRepository {

    private static List<Currency> DB = new ArrayList<>();

    FakeCurrenciesDataAccessService() {
        // This is hardcoded and should be removed before production.
        Currency usd = new Currency("USD");
        usd.setExchangeRate("EUR", new BigDecimal("0.84"));
        Currency eur = new Currency("EUR");
        usd.setExchangeRate("USD", new BigDecimal("1.18"));
        DB.add(usd);
        DB.add(eur);
    }

    @Override
    public BigDecimal getRate(String sourceCurrency, String destinationCurrency) {
        Optional<Currency> source = DB.stream()
                .filter(
                        currency -> currency.getSymbol().equals(sourceCurrency))
                .findFirst();
        if (!source.isPresent()) {
            throw new UnsupportedCurrencyException(sourceCurrency);
        }

        return source.get().getRates().get(destinationCurrency);
    }
}
