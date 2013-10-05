 /*
  *  This library is free software: you can redistribute it and/or modify
  *  it under the terms of the GNU Lesser General Public License as published by
  *  the Free Software Foundation, either version 3 of the License, or
  *  (at your option) any later version.
  *
  *  This library is distributed in the hope that it will be useful,
  *  but WITHOUT ANY WARRANTY; without even the implied warranty of
  *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  *  GNU Lesser General Public License for more details.
  *
  *  You should have received a copy of the GNU Lesser General Public License
  *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
  */

package com.sergeyorshanskiy.util;
import java.io.*
import java.net.*
import java.util.ArrayList
     		
     /**
     * This class contains a static method to connect to Yahoo finance and to retrieve stock quotes from it.
     * 
     * @author	Sergey Orshanskiy
     */
  
    public class YahooFinanceAPI {

     /**
     *
     * Connects to Yahoo finance and trieves stock quotes for the given ticker names.
     *
     * @param      List of stock tickers, e.g. ["AAPL", "MSFT", "GOOG"].
     * @return     Dictonary with [bid, ask] for each name, e.g. ["AAPL": [482.55, 482.74], "MSFT": [33.83, 33.85]]
     *
     */
    public static getBidAsk(ArrayList stocks) {
        def resultDict = [:]
        try
        {

	        // format options for the yahoo finance csv API: http://www.gummy-stuff.org/Yahoo-data.htm
            URL yahoofinance = new URL("http://finance.yahoo.com/d/quotes.csv?s=${stocks.join(",")}&f=ba"); // each line is bid,ask for a stock, e.g. "871.62,872.77"
            URLConnection yc = yahoofinance.openConnection();
            
	        BufferedReader inp = new BufferedReader(new InputStreamReader(yc.getInputStream()));
            String inputLine;
            def bid_ask_regex = ~/^(([\d]+(.[\d]+)?)|(N\/A)),(([\d]+(.[\d]+)?)|(N\/A))[ ]*$/    // two comma-separated real numbers; each can be N/A
	        def iter = stocks.iterator()
            while ((inputLine = inp.readLine()) != null)
            {
                // println inputLine
		        assert iter.hasNext()
                def stock=iter.next()
                assert inputLine ==~ bid_ask_regex, "Check if this URL correctly uses Yahoo finance csv api to retrieve \"bid,ask\": ${yahoofinance}; stocks:${stocks}"
	            def l = inputLine.split(',')
		        resultDict[stock] = [l[0]=='N/A' ? null : l[0].toDouble(), l[1]=='N/A' ? null : l[1].toDouble()]
                
            }    
	        assert !iter.hasNext()   // there should be a line for each stock, nothing extra
            inp.close();
        } catch (IOException ex) {
	        // Perhaps something is wrong with the Internet connection
            return null   
        }
        return resultDict
    }

     // TODO: make a JUnit test instead? 
     // Note that the result of the test depends on whether you can connect to Yahoo Finance, so not exactly a "unit test" as it is
    public static void main(String [] args) {
        println "Testing itself..."   
        def map = getBidAsk(["AAPL", "MSFT", "GOOG"])
        if (map) {
       	    map.each{ k, v -> println "${k}: bid=${v[0]}, ask=${v[1]}, aver=${(v[0] && v[1]) ? (v[0]+v[1])/2 : null}" }
        } else {
            println "Cannot retrieve data from Yahoo finance"
	        System.exit(1)
        }
    }
}

