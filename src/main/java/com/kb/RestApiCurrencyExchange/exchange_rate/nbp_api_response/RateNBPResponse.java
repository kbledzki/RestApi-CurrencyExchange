package com.kb.RestApiCurrencyExchange.exchange_rate.nbp_api_response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RateNBPResponse(
        String no,
        LocalDate effectiveDate,
        BigDecimal mid,
        BigDecimal bid,
        BigDecimal ask
) {
}