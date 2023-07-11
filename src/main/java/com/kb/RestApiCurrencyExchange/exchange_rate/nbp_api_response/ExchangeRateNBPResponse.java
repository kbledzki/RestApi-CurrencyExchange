package com.kb.RestApiCurrencyExchange.exchange_rate.nbp_api_response;

import com.kb.RestApiCurrencyExchange.enumrates.Currency;
import java.util.List;

public record ExchangeRateNBPResponse(
        Currency currencyCode,
        List<RateNBPResponse> rates
        ) {
        }
