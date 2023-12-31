package com.kb.RestApiCurrencyExchange.service;

import com.kb.RestApiCurrencyExchange.enumrates.Currency;
import com.kb.RestApiCurrencyExchange.exchange_rate.average.AverageExchangeRateResponse;
import com.kb.RestApiCurrencyExchange.exchange_rate.diffrence.BidAskDifferenceResponse;
import com.kb.RestApiCurrencyExchange.exchange_rate.min_max.MinMaxAverageValueResponse;
import com.kb.RestApiCurrencyExchange.exchange_rate.nbp_api_response.ExchangeRateNBPResponse;
import com.kb.RestApiCurrencyExchange.exchange_rate.nbp_api_response.RateNBPResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CurrencyExchangeService {
    private static final String NBP_API_BASE_URL = "http://api.nbp.pl/api/exchangerates/rates/";
    private static final String EXCHANGE_RATE_TABLE_A_API_URL = NBP_API_BASE_URL + "A/%s/%s";
    private static final String EXCHANGE_RATE_TABLE_C_API_URL = NBP_API_BASE_URL + "C/%s/%s";
    private final RestTemplate restTemplate;

    public AverageExchangeRateResponse getAverageExchangeRateByDateAndCurrency(String currencyCode, LocalDate date) {
        Currency currency = parseCurrencyCode(currencyCode);
        String exchangeRateApiUrl = String.format(EXCHANGE_RATE_TABLE_A_API_URL, currency, formatDateToString(date));
        ExchangeRateNBPResponse response = getExchangeRateApiResponse(exchangeRateApiUrl);

        return AverageExchangeRateResponse.builder()
                .currencyCode(currency)
                .currencyName(currency.getDescription())
                .date(date)
                .averageExchangeRate(getAverageExchangeRate(response))
                .build();
    }

    private String formatDateToString(LocalDate date) {
        return date.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    private BigDecimal getAverageExchangeRate(ExchangeRateNBPResponse response) {
        return response.rates().stream().findFirst()
                .map(RateNBPResponse::mid)
                .orElseThrow(() -> new NoSuchElementException("Cannot get mid value from received data."));
    }

    public MinMaxAverageValueResponse getMinMaxAverageValueForXDays(String currencyCode, int topCount) {
        Currency currency = parseCurrencyCode(currencyCode);
        String exchangeRateApiUrl = String.format(EXCHANGE_RATE_TABLE_A_API_URL, currency + "/last", topCount);
        ExchangeRateNBPResponse response = getExchangeRateApiResponse(exchangeRateApiUrl);

        return MinMaxAverageValueResponse.builder()
                .currencyCode(currency)
                .currencyName(currency.getDescription())
                .minAvgValue(getMinAvgRateFromExchangeRateResponse(response))
                .maxAvgValue(getMaxAvgRateFromExchangeRateResponse(response))
                .build();
    }

    private BigDecimal getMinAvgRateFromExchangeRateResponse(ExchangeRateNBPResponse response) {
        return Collections.min(getAvgRatesFromExchangeRateResponse(response));
    }

    private BigDecimal getMaxAvgRateFromExchangeRateResponse(ExchangeRateNBPResponse response) {
        return Collections.max(getAvgRatesFromExchangeRateResponse(response));
    }

    private List<BigDecimal> getAvgRatesFromExchangeRateResponse(ExchangeRateNBPResponse response) {
        return response.rates().stream()
                .map(RateNBPResponse::mid)
                .toList();
    }

    public BidAskDifferenceResponse getMajorDifferenceBetweenBuyAndAskRate(String currencyCode, int quotations) {
        com.kb.RestApiCurrencyExchange.enumrates.Currency currency = parseCurrencyCode(currencyCode);
        String exchangeRateApiUrl = String.format(EXCHANGE_RATE_TABLE_C_API_URL, currency + "/last", quotations);
        ExchangeRateNBPResponse response = getExchangeRateApiResponse(exchangeRateApiUrl);
        Map.Entry<LocalDate, BigDecimal> biggestDifference = getBuyAskMajorDifference(response);

        return BidAskDifferenceResponse.builder()
                .currencyCode(currency)
                .currencyName(currency.getDescription())
                .date(biggestDifference.getKey())
                .majorDifference(biggestDifference.getValue())
                .build();
    }

    private static Map.Entry<LocalDate, BigDecimal> getBuyAskMajorDifference(ExchangeRateNBPResponse response) {
        return getBuyAskDifferenceByDate(response).entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElseThrow(() -> new NoSuchElementException("Cannot get max value from received data."));
    }

    private static Map<LocalDate, BigDecimal> getBuyAskDifferenceByDate(ExchangeRateNBPResponse response) {
        return response.rates().stream()
                .collect(Collectors.toMap(
                        RateNBPResponse::effectiveDate,
                        rate -> rate.ask().subtract(rate.bid())
                ));
    }

    private ExchangeRateNBPResponse getExchangeRateApiResponse(String exchangeRateApiUrl) {
        return Optional.ofNullable(restTemplate.exchange(exchangeRateApiUrl, HttpMethod.GET, null,
                        new ParameterizedTypeReference<ExchangeRateNBPResponse>() {
                        }).getBody())
                .orElseThrow(() -> new NoSuchElementException("Cannot get exchange rate response from received data."));
    }

    private com.kb.RestApiCurrencyExchange.enumrates.Currency parseCurrencyCode(String currencyCode) {
        if (isValidCurrencyCode(currencyCode))
            return com.kb.RestApiCurrencyExchange.enumrates.Currency.valueOf(currencyCode);
        else throw new IllegalArgumentException("Wrong currencyCode: " + currencyCode);
    }

    private boolean isValidCurrencyCode(String code) {
        return Arrays.stream(com.kb.RestApiCurrencyExchange.enumrates.Currency.values())
                .anyMatch(c -> c.name().equals(code));
    }
}