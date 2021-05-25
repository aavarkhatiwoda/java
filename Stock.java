import java.io.*;
import java.net.*;
import jdk.tools.jlink.resources.plugins;

/*

WHAT THIS APP DOES:
This app lists the full company name and the current share price of
each ticker in the stocks() method. Webscraped from MarketWatch,
www.marketwatch.com.

Created with emphasis on scalability in mind. Adding or removing a
ticker only takes one line. Methods are used when efficient.

Recommended run from VS Code or terminal.

______________________________________________________________________________
SAMPLE OUTPUT:

TSLA
Tesla Inc.
$611.75/share


AAPL
Apple Inc.
$127.89/share


GME
GameStop Corp. Cl A
$181.74/share


AMC
AMC Entertainment Holdings Inc. Cl A
$13.57/share


UAL
United Airlines Holdings Inc.
$56.02/share


DAL
Delta Air Lines Inc.
$46.12/share


AAL
American Airlines Group Inc.
$22.92/share


ERRORTESTING
--- TICKER ERRORTESTING INVALID ---
$-999.99/share

______________________________________________________________________________

*/

public class Stock {
    
    String ticker;
    String companyName;
    double sharePrice = -999.99;

    public Stock(String ticker) { this.ticker = ticker; }

    private BufferedReader initiateBR() throws MalformedURLException, IOException {
        return new BufferedReader(
                    new InputStreamReader(
                        new URL(String.format("https://www.marketwatch.com/investing/stock/%s",ticker)).openConnection().getInputStream()
                    )
        );
    }

    public String getCompanyName() throws IOException {

        BufferedReader br = initiateBR();
        String line = br.readLine();
        companyName = String.format("--- TICKER %s INVALID ---",ticker);

        while (line != null) {
            if (line.contains("meta name=\"name\"")) { companyName = line.substring(line.indexOf("content=")+9, line.indexOf("\" />")); break; }
            line = br.readLine();
        }
        return companyName;
    }

    public double getSharePrice() throws IOException {
        
        BufferedReader br = initiateBR();
        String line = br.readLine();

        while (line != null) {
            if (line.contains("meta name=\"price\"")) { sharePrice = Double.parseDouble( line.substring(line.indexOf("$")+1, line.indexOf("\" /")) ); break; }
            line = br.readLine();
        }
        return sharePrice;
    }

     private static Stock[] stocks() {
        Stock[] s = new Stock[] {
            /* ENTER STOCK TICKERS AS DESIRED: new Stock("<TICKER>"), */
            new Stock("TSLA"),
            new Stock("AAPL"),
            new Stock("GME"),
            new Stock("AMC"),
            new Stock("UAL"),
            new Stock("DAL"),
            new Stock("AAL"),
            new Stock("ERRORTESTING"),
        };
        return s;
    }

    private static void printStocks(Stock[] s) throws IOException {
        for (int i = 0; i < s.length; i++) { s[i].getCompanyName(); s[i].getSharePrice(); System.out.println(s[i]); }
    }

    @Override public String toString() {
        return String.format("\n%s\n%s\n$%s/share\n", ticker, companyName, String.format("%.2f", sharePrice));
    }
    public static void main(String[] args) throws IOException {
        printStocks(stocks());
    }

}