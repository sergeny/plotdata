package com.sergeyorshanskiy.domain
import com.sergeyorshanskiy.util.YahooFinanceAPI;
import com.sergeyorshanskiy.domain.*;

class StockPrice extends DataPoint {

    String ticker; // Stock ticker, e.g. "AMZN" or "MSFT". This way we can have prices for different tickers in the same SQL table.

    static constraints = {
    }

    static StockPriceFactory getFactory(String ticker) {
        new StockPriceFactory(ticker);
    }
}

class StockPriceFactory extends DataPointFactory {

    String ticker; // Stock ticker, e.g. "AMZN" or "MSFT"

    public StockPriceFactory(String ticker) {
        this.ticker = ticker;    
    }
    
    StockPrice generate() {
        println "getting bid, ask for ${ticker}"
        def l = YahooFinanceAPI.getBidAsk([ticker])[ticker]
        if (l[0] || l[1]) { // at least the bid or the ask if defined
            // if both are defined, average bid and ask. Otherwise use whatever is available
            def price = l[0] ? (l[1] ? (l[0] + l[1]) / 2 : l[0]) : l[1] 
            new StockPrice(ticker: this.ticker, x: 5, y: price)
        } else {
            return null
        }
    }
}