package model.portfolio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import model.stocks.IStocks;
import model.stocks.Stocks;

/**
 * Class for flexible portfolio.
 */
public class FlexiblePortfolio implements IFlexiblePortfolio {

  protected HashMap<String, HashMap<String, HashMap<String, List<String>>>> map = new HashMap<>();
  protected HashMap<String, HashMap<String, Double>> costBasis
      = new HashMap<String, HashMap<String, Double>>();
  protected String portfolioName;
  protected double totalValue;
  protected double commissionFee;

  /**
   * Constructor for flexible portfolio.
   */
  public FlexiblePortfolio() {
    this.portfolioName = "";
    this.totalValue = 0;
    this.commissionFee = 0.00;
  }

  @Override
  public void createPortfolio(String portfolioName, String date) {
    if (checkPortfolioAlreadyExists(portfolioName)) {
      throw new IllegalArgumentException(
          "PORTFOLIO ALREADY PRESENT. ADD STOCKS.");
    }
    this.map.put(portfolioName, new HashMap<>());
    this.costBasis.put(portfolioName, new HashMap<>());
    this.costBasis.get(portfolioName).put(date, 0.00);
    this.portfolioName = portfolioName;
  }

  @Override
  public boolean checkPortfolioAlreadyExists(String name) {
    return map.containsKey(name);
  }

  @Override
  public void buyStock(String portfolioName, String ticker, int quantity, double price, String date,
      double fee) {
    this.commissionFee = fee;
    if (!map.containsKey(portfolioName)) {
      throw new IllegalArgumentException("Enter valid portfolio name.");
    }
    IStocks stocks = new Stocks();
    date = stocks.isWeekend(date);
    if (map.get(portfolioName).containsKey(date)) {
      if (map.get(portfolioName).get(date).containsKey(ticker)) {
        int existingNoOfStocks = Integer.parseInt(
            map.get(portfolioName).get(date).get(ticker).get(0));
        map.get(portfolioName).get(date).get(ticker)
            .set(0, String.valueOf(existingNoOfStocks + quantity));
        double existingPrice = Double.parseDouble(
            map.get(portfolioName).get(date).get(ticker).get(1));
        map.get(portfolioName).get(date).get(ticker)
            .set(1, String.valueOf((existingPrice + price) / 2));
        double existingTotalStockValue = Double.parseDouble(
            map.get(portfolioName).get(date).get(ticker).get(2));
        map.get(portfolioName).get(date).get(ticker)
            .set(2, String.valueOf(existingTotalStockValue + (quantity * price)));
        this.totalValue = totalValue + Math.round(quantity * price) + commissionFee;
      } else {
        addStockDataHelper(portfolioName, ticker, quantity, price, date, 0);
        this.totalValue = totalValue + (quantity * price) + commissionFee;
      }

    } else {
      if (map.get(portfolioName).keySet().size() > 0) {
        String prevDate = getPreviousDate(date, portfolioName);
        map.get(portfolioName).put(date, new HashMap<>());
        for (String stock : map.get(portfolioName).get(prevDate).keySet()) {

          map.get(portfolioName).get(date).put(stock, new ArrayList<>());
          map.get(portfolioName).get(date).get(stock)
              .add(map.get(portfolioName).get(prevDate).get(stock).get(0));
          map.get(portfolioName).get(date).get(stock)
              .add(map.get(portfolioName).get(prevDate).get(stock).get(1));
          map.get(portfolioName).get(date).get(stock)
              .add(map.get(portfolioName).get(prevDate).get(stock).get(2));
        }
        if (map.get(portfolioName).get(date).containsKey(ticker)) {
          int existingQuantity = Integer.parseInt(
              map.get(portfolioName).get(date).get(ticker).get(0));
          map.get(portfolioName).get(date).get(ticker)
              .set(0, String.valueOf(existingQuantity + quantity));
          double existingPrice = Integer.parseInt(
              map.get(portfolioName).get(date).get(ticker).get(1));
          double existingTotalValue = Integer.parseInt(
              map.get(portfolioName).get(date).get(ticker).get(2));
          map.get(portfolioName).get(date).get(ticker).set(1, String.valueOf(
              (existingTotalValue + (quantity * price)) / (existingQuantity + quantity)));
          map.get(portfolioName).get(date).get(ticker)
              .set(2, String.valueOf((existingTotalValue + (quantity * price))));
          this.totalValue = totalValue + (quantity * price) + commissionFee;
        } else {
          addStockDataHelper(portfolioName, ticker, quantity, price, date, 0);
          this.totalValue = totalValue + (quantity * price) + commissionFee;
        }
      } else {
        map.get(portfolioName).put(date, new HashMap<>());
        addStockDataHelper(portfolioName, ticker, quantity, price, date, 0);
        this.totalValue = totalValue + (quantity * price) + commissionFee;
      }
    }
    costBasis.get(portfolioName).put(date, totalValue); // check if overwrties
    updateCostBasisRecords(portfolioName, date, totalValue);
    updateFutureRecords(portfolioName, ticker, date, quantity, price);
  }

  private void updateFutureRecords(String portfolioName, String ticker, String date, int quantity,
      double price) {
    List<String> dates = sortDateHelper(map.get(portfolioName).keySet(), portfolioName);
    for (String s : dates) {
      if (s.compareTo(date) > 0) {
        if (map.get(portfolioName).get(s).containsKey(ticker)) {
          if (quantity == 0) {
            map.get(portfolioName).get(s).remove(ticker);
          } else {
            map.get(portfolioName).get(s).put(ticker, new ArrayList<>());
            map.get(portfolioName).get(s).get(ticker).add(String.valueOf(quantity));
            map.get(portfolioName).get(s).get(ticker).add(String.valueOf(price));
            map.get(portfolioName).get(s).get(ticker).add(String.valueOf(quantity * price));
          }
        }
      }
    }
  }

  private void updateCostBasisRecords(String portfolioName, String date, double value) {
    List<String> dates = sortDateHelper(costBasis.get(portfolioName).keySet(), portfolioName);
    for (String s : dates) {
      if (s.compareTo(date) > 0) {
        costBasis.get(portfolioName).put(s, value);
      }
    }
  }

  private List<String> sortDateHelper(Set<String> strings, String portfolioName) {
    List<String> dates = new ArrayList<>();
    ListIterator<String> dateIterator = new ArrayList<String>(strings).listIterator();
    while (dateIterator.hasNext()) {
      dates.add(dateIterator.next());
    }
    Collections.sort(dates);
    return dates;
  }

  private void addStockDataHelper(String portfolioName, String ticker, int quantity, double price,
      String date, int previousQuantity) {
    map.get(portfolioName).get(date).put(ticker, new ArrayList<>());
    map.get(portfolioName).get(date).get(ticker).add(String.valueOf(previousQuantity + quantity));
    map.get(portfolioName).get(date).get(ticker).add(String.valueOf(price));
    map.get(portfolioName).get(date).get(ticker).add(String.valueOf(quantity * price));
  }

  @Override
  public void sellStock(String portfolioName, String ticker, int quantity, double price,
      String date, double fee) {
    if (!map.containsKey(portfolioName)) {
      throw new IllegalArgumentException("Enter valid portfolio name.");
    }
    this.commissionFee = fee;
    String prevdate = getPreviousDate(date, portfolioName);
    if (map.get(portfolioName).get(prevdate).containsKey(ticker)) {
      int existingNoOfStocks = Integer.parseInt(
          map.get(portfolioName).get(prevdate).get(ticker).get(0));
      if (quantity == existingNoOfStocks) {
        map.get(portfolioName).remove(ticker);
      } else if (quantity > existingNoOfStocks) {
        throw new IllegalArgumentException("NOT ENOUGH STOCKS PRESENT IN PORTFOLIO");
      } else {
        map.get(portfolioName).get(prevdate).get(ticker)
            .set(0, String.valueOf(existingNoOfStocks - quantity));
        double existingPrice = Double.parseDouble(
            map.get(portfolioName).get(prevdate).get(ticker).get(1));
        map.get(portfolioName).get(prevdate).get(ticker)
            .set(1, String.valueOf((existingPrice + price) / 2)); // chrck
        double existingTotalStockValue = Double.parseDouble(
            map.get(portfolioName).get(prevdate).get(ticker).get(2));
        map.get(portfolioName).get(prevdate).get(ticker)
            .set(2, String.valueOf(existingTotalStockValue - (quantity * price)));
        this.totalValue = totalValue - Math.round(quantity * price);
      }
      updateFutureRecords(portfolioName, ticker, date, quantity, price);
    } else {
      throw new IllegalArgumentException("No such stock present.");
    }
  }

  @Override
  public double costBasisByDate(String portFolioName, String date) {
    return costBasis.get(portfolioName).get(date);
  }


  @Override
  public double portfolioValue(String portfolioName, String date) {
    double value = 0;
    List<String> dates = sortDateHelper(map.get(portfolioName).keySet(), portfolioName);
    if (dates.get(0).equals(date)) {
      return value;
    }
    for (String stock : map.get(portfolioName).get(date).keySet()) {
      value += Double.parseDouble(map.get(portfolioName).get(date).get(stock).get(2));
    }
    return value;
  }

  @Override
  public HashMap<String, HashMap<String, HashMap<String, List<String>>>> returnMap() {
    return map;
  }

  @Override
  public HashMap<String, HashMap<String, Double>> returnCostBasisMap() {
    return costBasis;
  }

  @Override
  public String getPreviousDate(String currentDate, String name) {
    List<String> dates = new ArrayList<>();
    ListIterator<String> dateIterator = new ArrayList<String>(
        map.get(name).keySet()).listIterator();
    if (map.get(name).keySet().size() == 1) {
      return map.get(name).keySet().iterator().next();
    }
    while (dateIterator.hasNext()) {
      dates.add(dateIterator.next());
    }
    Collections.sort(dates);
    if (dates.get(0).equals(currentDate)) {
      return currentDate;
    }
    for (int i = 1; i < map.get(name).keySet().size(); i++) {
      if (dates.get(i).compareTo(currentDate) > 0) {
        return dates.get(i - 1);
      }
    }
    return dates.get(dates.size() - 1);
  }
}
