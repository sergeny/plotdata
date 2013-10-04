import java.io.*
import java.net.*

  try
        {
            String[] stocks = ["GOOG", "MSFT"]
	    // format options for the yahoo finance API: http://www.gummy-stuff.org/Yahoo-data.htm
            URL yahoofinance = new URL("http://finance.yahoo.com/d/quotes.csv?s=" + stocks[0] + "+" + stocks[1] + "&f=ba"); // bid,ask, e.g. "871.62,872.77"
            URLConnection yc = yahoofinance.openConnection();

            
	    BufferedReader inp = new BufferedReader(new InputStreamReader(
                    yc.getInputStream()));
            String inputLine;
            while ((inputLine = inp.readLine()) != null)
            {
                System.out.println(inputLine);
            }
            inp.close();
        } catch (IOException ex)
        {
            System.out.println("Oops bad things happens");
        }