package com.kb.RestApiCurrencyExchange.exchange_rate.min_max;

import com.kb.RestApiCurrencyExchange.enumrates.Currency;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record MinMaxAverageValueResponse(
        Currency currencyCode,
        String currencyName,
        BigDecimal minAvgValue,
        BigDecimal maxAvgValue
) {
}