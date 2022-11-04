package model.stocks;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

/**
 * This is the class that calls the Alpha Vantage API to fetch stock data.
 */
public class FetchStockData {

  String[] stockData;

  public FetchStockData() {
    this.stockData = new String[100];
  }

  /**
   * This method will call the API and update the class attribute with the data.
   *
   * @param: ticker
   * @return: array of string with stock data
   */
  public String[] callStockAPI(String ticker) {
    String apiKey = "DRH4BIM55S9Y82AI";
    URL url = null;

    try {
      url = new URL("https://www.alphavantage"
          + ".co/query?function=GLOBAL_QUOTE"
          + "&outputsize=1"
          + "&symbol"
          + "=" + ticker + "&apikey=" + apiKey + "&datatype=csv");
    } catch (MalformedURLException e) {
      throw new RuntimeException("the alphavantage API has either changed or "
          + "no longer works");
    }
    InputStream in = null;
    StringBuilder output = new StringBuilder();
    try {
      in = url.openStream();
      int b;
      while ((b = in.read()) != -1) {
        output.append((char) b);
      }
    } catch (IOException e) {
      throw new IllegalArgumentException("No price data found for " + ticker);
    }
    this.stockData = output.toString().split(",");
    return this.stockData;
  }

  /**
   * This method will call the API and update the class attribute with the data at a given date.
   *
   * @param: ticker
   * @return: array of string with stock data
   */
  public String[] callStockAPIbyDate(String ticker, String date) {
    String apiKey = "DRH4BIM55S9Y82AI";
    URL url = null;

    try {
      url = new URL("https://www.alphavantage"
          + ".co/query?function=TIME_SERIES_DAILY"
          + "&outputsize=full"
          + "&symbol"
          + "=" + ticker + "&apikey=" + apiKey + "&datatype=csv");
    } catch (MalformedURLException e) {
      throw new RuntimeException("the alphavantage API has either changed or "
          + "no longer works");
    }

    InputStream in = null;
    StringBuilder output = new StringBuilder();

    try {
      in = url.openStream();
      int b;

      while ((b = in.read()) != -1) {
        output.append((char) b);
      }
    } catch (IOException e) {
      throw new IllegalArgumentException("No price data found for " + ticker);
    }
    HashMap<String, List<String>> map = new HashMap<String, List<String>>();
    String[] introduction = output.toString().split("[\\r\\n]");
    String storeTemp = "";
    for (int i = 0; i < introduction.length; i++) {
      if (introduction[i].contains(date)) {
        storeTemp += introduction[i];
        break;
      }
    }
    this.stockData = storeTemp.split(",");
    return this.stockData;
  }
}
