import java.io.*
import java.net.*

  try
        {
            String[] stocks = ["GOOG", "MSFT", "AAPL"]
	    // format options for the yahoo finance csv API: http://www.gummy-stuff.org/Yahoo-data.htm
            URL yahoofinance = new URL("http://finance.yahoo.com/d/quotes.csv?s=${stocks.join(",")}&f=ba"); // each line is bid,ask for a stock, e.g. "871.62,872.77"
            URLConnection yc = yahoofinance.openConnection();
            
	    BufferedReader inp = new BufferedReader(new InputStreamReader(yc.getInputStream()));
            String inputLine;
            def bid_ask_regex = ~/^([\d]+(.[\d]+)?),([\d]+(.[\d]+)?)[ ]*$/    // two comma-separated real numbers
	    iter = stocks.iterator()
	    while ((inputLine = inp.readLine()) != null)
            {
                // println inputLine
		assert inputLine ==~ bid_ask_regex, "Check if this URL correctly uses Yahoo finance csv api to retrieve \"bid,ask\": ${yahoofinance}; stocks:${stocks}"
		def l = inputLine.split(',')
		assert iter.hasNext()
		def stock=iter.next()
		println "${stock} bid: ${l[0].toDouble()}, ask: ${l[1].toDouble()}"
            }
	    assert !iter.hasNext()   // should read a line for each stock
            inp.close();
        } catch (IOException ex)
        {
            println "Something went wrong..."
        }