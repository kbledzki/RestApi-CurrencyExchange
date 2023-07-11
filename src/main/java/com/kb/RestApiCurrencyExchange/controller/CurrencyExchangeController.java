package com.kb.RestApiCurrencyExchange.controller;

import com.kb.RestApiCurrencyExchange.exchange_rate.average.AverageExchangeRateResponse;
import com.kb.RestApiCurrencyExchange.exchange_rate.diffrence.BidAskDifferenceResponse;
import com.kb.RestApiCurrencyExchange.exchange_rate.min_max.MinMaxAverageValueResponse;
import com.kb.RestApiCurrencyExchange.service.CurrencyExchangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/exchange-rates")
@RequiredArgsConstructor
public class CurrencyExchangeController {
    private final CurrencyExchangeService service;


    @GetMapping("average/{currencyCode}/{date}")
    public ResponseEntity<AverageExchangeRateResponse> getAverageExchangeRate(@PathVariable("currencyCode") String currencyCode,
                                                                              @PathVariable("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date
    ) {
        return ResponseEntity.ok(
                service.getAverageExchangeRateByDateAndCurrency(currencyCode, date));
    }

    @GetMapping("/min-max/{currencyCode}/{numOfQuotes}")
    public ResponseEntity<MinMaxAverageValueResponse> getMinMaxAverageValue(@PathVariable("currencyCode") String currencyCode,
                                                                            @PathVariable("numOfQuotes") int numOfQuotes) {
        return ResponseEntity.ok(
                service.getMinMaxAverageValueForXDays(currencyCode, numOfQuotes));
    }

    @GetMapping("/difference/{currencyCode}/{numOfQuotes}")
    public ResponseEntity<BidAskDifferenceResponse> getMajorDifference(@PathVariable("currencyCode") String currencyCode,
                                                                       @PathVariable("numOfQuotes") int numOfQuotes) {
        return ResponseEntity.ok(
                service.getMajorDifferenceBetweenBuyAndAskRate(currencyCode, numOfQuotes));
    }
}
