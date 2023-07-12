# RestApi-CurrencyExchange
Connect with Narodowy Bank Polski's public APIs and returns relevant information from them. The API supports three operations:

### Operation 1: Average exchange rate given currency code and date (formatted YYYY-MM-DD)
   * Endpoint:
        
          GET /api/v1/exchange-rates/average/{currencyCode}/{date}
       
    * Example:
        
          GET /api/v1/exchange-rates/average/GBP/2023-05-01

### Operation 2: Max and min average value given currency code and the number of last quotations N 
  * Endpoint: 
   
         GET /api/v1/exchange-rates/min-max/{currencyCode}/{numOfQuotes}

   * Example:
        
         GET /api/v1/exchange-rates/min-max/GBP/10
    

### Operation 3: Major difference between buy and ask rate given currency code and the number of last quotations N 
   * Endpoint: 
   
         GET /api/v1/exchange-rates/difference/{currencyCode}/{numOfQuotes}

   * Example:
        
         GET /api/v1/exchange-rates/difference/USD/10
     
