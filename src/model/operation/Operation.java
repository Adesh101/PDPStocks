package model.operation;

import model.stocks.IStocks;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class contains all the major methods that are required for the stock software.
 */
public class Operation implements IOperation {

  protected HashMap<String, HashMap<String, List<String>>> portfolios
      = new HashMap<String, HashMap<String, List<String>>>();
  protected String portfolioName;
  protected double totalValue;
  protected IStocks stocks;
  private static final String CSV_SEPARATOR = ",";


  /**
   * Constructor for Operation class.
   *
   * @param: stocks
   */
  public Operation(IStocks stocks) {
    this.portfolioName = "";
    this.totalValue = 0;
    this.stocks = stocks;
  }

  @Override
  public void createNewPortfolio(String portfolioName) {
    if (checkPortfolioAlreadyExists(portfolioName)) {
      if (getMapSize(portfolioName) != 0) {
        throw new IllegalArgumentException("CANNOT MODIFY A LOCKED PORTFOLIO.");
      } else {
        throw new IllegalArgumentException(
            "PORTFOLIO ALREADY PRESENT. ADD STOCKS.");
      }
    }
    this.portfolios.put(portfolioName, new HashMap<String, List<String>>());
    this.portfolioName = portfolioName;
  }

  @Override
  public void addStockToPortfolio(String portfolioName, String ticker, int quantity, double price) {
    if (!portfolios.containsKey(portfolioName)) {
      throw new IllegalArgumentException("Enter valid portfolio name.");
    }
    if (portfolios.get(portfolioName).containsKey(ticker)) {
      int existingNoOfStocks = Integer.parseInt(portfolios.get(portfolioName).get(ticker).get(0));
      portfolios.get(portfolioName).get(ticker)
          .set(0, String.valueOf(existingNoOfStocks + quantity));
      double existingPrice = Double.parseDouble(portfolios.get(portfolioName).get(ticker).get(1));
      portfolios.get(portfolioName).get(ticker).set(1, String.valueOf((existingPrice + price) / 2));
      double existingTotalStockValue = Double.parseDouble(
          portfolios.get(portfolioName).get(ticker).get(2));
      portfolios.get(portfolioName).get(ticker)
          .set(2, String.valueOf(existingTotalStockValue + (quantity * price)));
      this.totalValue = totalValue + Math.round(quantity * price);
    } else {
      portfolios.get(portfolioName).put(ticker, new ArrayList<String>());
      portfolios.get(portfolioName).get(ticker).add(String.valueOf(quantity));
      portfolios.get(portfolioName).get(ticker).add(String.valueOf(price));
      portfolios.get(portfolioName).get(ticker).add(String.valueOf(quantity * price));
      this.totalValue = totalValue + (quantity * price);
    }
  }

  @Override
  public boolean checkPortfolioAlreadyExists(String name) {
    return portfolios.containsKey(name);
  }

  @Override
  public String getExistingPortfolios() {
    if (portfolios.size() == 0) {
      throw new IllegalArgumentException("NO AVAILABLE PORTFOLIOS TO DISPLAY.");
    }
    StringBuilder allPortfolios = new StringBuilder();
    for (String portfolioNames : portfolios.keySet()) {
      allPortfolios.append(portfolioNames + "\n");
    }
    return allPortfolios.toString();
  }

  @Override
  public double getCurrentPrice(String ticker) {
    return stocks.getStockCurrentPrice(ticker);
  }

  @Override
  public boolean isTickerValid(String ticker) {
    String[] stockData = stocks.callStockAPI(ticker);
    return stockData.length != 1;
  }

  @Override
  public void writeToCSV(String portfolioName) {
    try {
      BufferedWriter bw = new BufferedWriter(
          new OutputStreamWriter(new FileOutputStream("./res/" + portfolioName + ".csv"), "UTF-8"));
      HashMap<String, HashMap<String, List<String>>> portfolios = this.portfolios;
      for (String individualPortfolioName : portfolios.keySet()) {
        StringBuffer oneLine = new StringBuffer();
        oneLine.append("Portfolio Name");
        oneLine.append(CSV_SEPARATOR);
        oneLine.append(portfolioName);
        oneLine.append("\n");
        oneLine.append("Stock");
        oneLine.append(CSV_SEPARATOR);
        oneLine.append("Quantity");
        oneLine.append(CSV_SEPARATOR);
        oneLine.append("Price");
        oneLine.append(CSV_SEPARATOR);
        oneLine.append("Total");
        oneLine.append(CSV_SEPARATOR);
        oneLine.append("\n");
        for (String stockData : portfolios.get(individualPortfolioName).keySet()) {
          oneLine.append(stockData);
          oneLine.append(CSV_SEPARATOR);
          for (String metaStockData : portfolios.get(individualPortfolioName).get(stockData)) {
            oneLine.append(metaStockData);
            oneLine.append(CSV_SEPARATOR);
          }
        }
        bw.write(oneLine.toString());
        bw.newLine();
      }
      bw.flush();
      bw.close();
    } catch (IOException e) {
      System.out.println("");
    }
  }

  @Override
  public String readFromFile(String fileName) {
    String line = "";
    String splitBy = ",";
    try {
      BufferedReader br = new BufferedReader(new FileReader(fileName));
      while ((line = br.readLine()) != null) {   //returns a Boolean value
        String[] portfolioNames = line.split(splitBy);    // use comma as separator
        if (portfolioNames.length == 1) {
          if (portfolioNames[0].length() != 0) {
            createNewPortfolio(portfolioNames[0]);
            this.portfolioName = portfolioNames[0];
            break;
          }
        }
      }
    } catch (Exception ex) {
      return "INVALID FILE.";
    }
    return "PORTFOLIO " + this.portfolioName + " SUCCESSFULLY CREATED.";
  }

  @Override
  public List<String> getStockNamesFromPortfolio() {
    List<String> list = new ArrayList<String>();
    for (String portfolioName : this.portfolios.keySet()) {
      for (String stockName : this.portfolios.get(portfolioName).keySet()) {
        list.add(stockName);
      }
    }
    return list;
  }

  @Override
  public HashMap<String, HashMap<String, List<String>>> getPortfolioMap() {
    return this.portfolios;
  }

  @Override
  public double callStockAPIByDateHelper(HashMap<String, List<String>> map, String date) {
    double currentValue = 0;
    for (String string : map.keySet()) {
      stocks.callStockAPIByDate(string, date);
      double tempValue = stocks.getStockCurrentPriceByDate(string);
      tempValue *= Double.parseDouble(map.get(string).get(0));
      currentValue += tempValue;
    }
    return currentValue;
  }


  @Override
  public int getMapSize(String portfolioName) {
    return this.portfolios.get(portfolioName).size();
  }

  @Override
  public String getPortfolioComposition(String portfolioName) {
    StringBuilder sb = new StringBuilder();
    String finalString = "";
    if (checkPortfolioAlreadyExists(portfolioName)) {
      sb.append("Portfolio : ").append(portfolioName).append("\n");
      sb.append("TICK - QTY - PRICE - TOTAL \n");
      for (String stockName : portfolios.get(portfolioName).keySet()) {
        sb.append(stockName).append(" - ");
        for (int i = 0; i < portfolios.get(portfolioName).get(stockName).size(); i++) {
          sb.append(portfolios.get(portfolioName).get(stockName).get(i)).append(" - ");
        }
      }
      if (sb.toString().endsWith("- ")) {
        finalString = sb.substring(0, sb.length() - 3);
      }
      return finalString;
    }
    throw new IllegalArgumentException("ENTER VALID PORTFOLIO NAME.");
  }

  @Override
  public double getPortfolioByDate(String portfolioName, String date) {
    HashMap<String, List<String>> map = new HashMap<String, List<String>>();
    String dateFormat = "yyyy-MM-dd";
    try {
      DateFormat df = new SimpleDateFormat(dateFormat);
      df.setLenient(false);
      df.parse(date);
      double totalValueByDate = 0;
      for (String string : this.portfolios.get(portfolioName).keySet()) {
        map.put(string, new ArrayList<>());
        map.get(string).add(portfolios.get(portfolioName).get(string).get(0));
      }
      double finalValue = callStockAPIByDateHelper(map, date);
      return finalValue;
    } catch (ParseException e) {
      throw new IllegalArgumentException("ENTER DATE IN YYYY-MM-DD FORMAT");
    }
  }
}
