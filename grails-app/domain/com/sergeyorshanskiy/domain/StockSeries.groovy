package com.sergeyorshanskiy.domain


// Stock price information for a particular ticker.
class StockSeries extends TimeSeries {

	String ticker; // e.g. "AAPL"

	// We are going to pick a series by ticker, so it better be unique. 
	// E.g. all Apple stock prices will be contained in just one StockSeries.
    static constraints = {
		ticker(unique: true)
    }

	void generate() {
		super.generate(7)
	}

	public String toString() {
		return "${this.class.name} : ${this.id}, ticker: ${ticker}"   // Cannot use super.toString(), wrong type
	}
}
