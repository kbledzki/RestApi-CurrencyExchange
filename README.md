# RestApi-CurrencyExchange

### Operation 1: Average exchange rate given currency code and date (formatted YYYY-MM-DD)
   * Endpoint:
        
          GET /api/v1/exchange-rates/average/{currencyCode}/{date}

### Operation 2: Max and min average value given currency code and the number of last quotations N (N <= 255)
  * Endpoint: 
   
         GET /api/v1/exchange-rates/min-max/{currencyCode}/{numOfQuotes}

### Operation 3: Major difference between buy and ask rate given currency code and the number of last quotations N (N <= 255)
   * Endpoint: 
   
         GET /api/v1/exchange-rates/difference/{currencyCode}/{numOfQuotes}
