package com.kb.RestApiCurrencyExchange.exchange_rate.average;

import com.kb.RestApiCurrencyExchange.enumrates.Currency;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record   AverageExchangeRateResponse(
        Currency currencyCode,
        String currencyName,
        LocalDate date,
        BigDecimal averageExchangeRate
) {
}
