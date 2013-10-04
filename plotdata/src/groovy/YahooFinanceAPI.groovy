import java.io.*
import java.net.*
import java.util.ArrayList


public class YahooFinanceAPI {

// input e.g. ["AAPL", "MSFT", "GOOG"] 
// output: dictionary, e.g. [AAPL:[482.55, 482.74], MSFT:[33.83, 33.85], GOOG:[871.12, 872.5]]

public static getBidAsk(ArrayList stocks) {
      def resultDict = [:]
 try
        {

	    // format options for the yahoo finance csv API: http://www.gummy-stuff.org/Yahoo-data.htm
            URL yahoofinance = new URL("http://finance.yahoo.com/d/quotes.csv?s=${stocks.join(",")}&f=ba"); // each line is bid,ask for a stock, e.g. "871.62,872.77"
            URLConnection yc = yahoofinance.openConnection();
            
	    BufferedReader inp = new BufferedReader(new InputStreamReader(yc.getInputStream()));
            String inputLine;
            def bid_ask_regex = ~/^([\d]+(.[\d]+)?),([\d]+(.[\d]+)?)[ ]*$/    // two comma-separated real numbers
	    def iter = stocks.iterator()
	    while ((inputLine = inp.readLine()) != null)
            {
                // println inputLine
		assert inputLine ==~ bid_ask_regex, "Check if this URL correctly uses Yahoo finance csv api to retrieve \"bid,ask\": ${yahoofinance}; stocks:${stocks}"
		def l = inputLine.split(',')
		assert iter.hasNext()
		def stock=iter.next()
		resultDict[stock] = [l[0].toDouble(), l[1].toDouble()]
            }
	    assert !iter.hasNext()   // should read a line for each stock
            inp.close();
        } catch (IOException ex)
        {
	    // Perhaps something is wrong with the Internet connection
            return null   
        }

	return resultDict

}

// TODO: make a JUnit test
// Note that the result of the test depends on whether you can connect to Yahoo Finance, so not exactly a "unit test" as it is
public static void main(String [] args) {
       println "Testing itself..."   
       def map = getBidAsk(["AAPL", "MSFT", "GOOG"])
       if (map) {
       	  map.each{ k, v -> println "${k}: bid=${v[0]}, ask=${v[1]}, aver=${(v[0]+v[1])/2}" }
       } else {
          println "Cannot retrieve data from Yahoo finance"
	  System.exit(1)
       }
}


}

