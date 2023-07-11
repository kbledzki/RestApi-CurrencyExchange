package com.kb.RestApiCurrencyExchange.exchange_rate.diffrence;

import com.kb.RestApiCurrencyExchange.enumrates.Currency;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record BidAskDifferenceResponse(
        Currency currencyCode,
        String currencyName,
        BigDecimal majorDifference,
        LocalDate date
) {
}
