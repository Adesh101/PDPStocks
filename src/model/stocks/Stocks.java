package model.stocks;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import model.filehandling.CsvFiles;

/**
 * This class is used to handle stock data that we fetch from an API.
 */
public class Stocks implements IStocks {

  /**
   * This method will call the API and update the class attribute with the data.
   *
   * @param: ticker
   * @return: array of string with stock data
   */
  public String[] callStockAPI(String ticker, String date) {
    CsvFiles file = new CsvFiles();
    String apiKey = "FIR1DN0VB7SQ4SGD";
    URL url = null;

    if (date.equals("")) {
      date = isWeekend(new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime()));
    }

    if (file.checkLocalData(ticker)) {
      date = isWeekend(date);
      return file.readFromLocalData(ticker, date);
    }

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
      file.addToLocalData(ticker, output);
    } catch (IOException e) {
      throw new IllegalArgumentException("No price data found for " + ticker);
    }
    return file.readFromLocalData(ticker, date);
  }

  @Override
  public String isWeekend(String date) {
    try {
      DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
      Calendar c1 = Calendar.getInstance();
      Date d1 = new SimpleDateFormat("yyyy-MM-dd").parse(date);
      c1.setTime(d1);
      if (c1.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
        c1.add(Calendar.DAY_OF_WEEK, -1);
        date = dateFormat.format(c1.getTime());
      } else if (c1.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
        c1.add(Calendar.DAY_OF_WEEK, -2);
        date = dateFormat.format(c1.getTime());
      }
    } catch (ParseException ex) {
      System.out.println(ex.getMessage());
    }
    return date;
  }

  @Override
  public void updateFile(String file) {
    CsvFiles files = new CsvFiles();
    String apiKey = "FIR1DN0VB7SQ4SGD";
    URL url = null;

    String tillDate = files.getMostRecentDate(file);
    if (!files.checkIfRecentDateIsCurrentDate(tillDate)) {
      try {
        url = new URL("https://www.alphavantage"
            + ".co/query?function=TIME_SERIES_DAILY"
            + "&outputsize=full"
            + "&symbol"
            + "=" + file + "&apikey=" + apiKey + "&datatype=csv");
      } catch (MalformedURLException e) {
        throw new RuntimeException("the alphavantage API has either changed or "
            + "no longer works");
      }
      InputStream in = null;
      StringBuilder output = new StringBuilder();
      try {
        in = url.openStream();
        int b;
        String newData = "";
        while ((b = in.read()) != -1) {
          output.append((char) b);
          if (output.toString().contains(tillDate)) {
            break;
          }
        }
        newData = output.substring(output.indexOf("\n") + 1, output.lastIndexOf("\r"));
        files.updateFile(file, newData);
      } catch (IOException e) {
        throw new IllegalArgumentException("No price data found for: " + file);
      }
    }
  }

  @Override
  public double getPriceByDate(String ticker, String date) {
    CsvFiles file = new CsvFiles();
    double price = 0;
    try {
      price = Double.parseDouble(file.readFromLocalData(ticker, date)[4]);
      return price;
    } catch (NullPointerException e) {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
      Date newDate;
      Calendar c = Calendar.getInstance();
      try {
        c.setTime(sdf.parse(date));
        c.add(Calendar.DATE, -1);
        newDate = sdf.parse(sdf.format(c.getTime()));
        String validDate = sdf.format(newDate);
        price = getPriceByDate(ticker, validDate);
      } catch (ParseException ex) {
        System.out.println(ex.getMessage());
      }
    }
    return price;
  }
}
