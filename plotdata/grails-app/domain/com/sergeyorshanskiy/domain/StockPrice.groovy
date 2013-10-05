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

    public static void main() {
        println 'hello world!'
    }
}

class StockPriceFactory extends DataPointFactory {

    String ticker; // Stock ticker, e.g. "AMZN" or "MSFT"

    public StockPriceFactory(String ticker) {
        this.ticker = ticker;    
    }
    
    StockPrice generate() {
        println "getting bid, ask for ${ticker}"
        def l = YahooFinanceAPI.getBidAsk([ticker])
        new StockPrice(ticker: this.ticker, x: 5, y: (l[0] + l[1]) / 2)
    }
}