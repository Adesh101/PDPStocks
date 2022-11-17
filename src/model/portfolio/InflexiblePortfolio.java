package model.portfolio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

/**
 * Class for inflexible portfolio.
 */
public class InflexiblePortfolio implements IInflexiblePortfolio {

  protected HashMap<String, HashMap<String, List<String>>> portfolios
      = new HashMap<String, HashMap<String, List<String>>>();
  protected HashMap<String, HashMap<String, HashMap<String, List<String>>>> map = new HashMap<>();
  protected String portfolioName;
  protected double totalValue;
  protected String date;

  /**
   * Constructor for inflexible portfolio.
   */
  public InflexiblePortfolio() {
    this.totalValue = 0;
    this.portfolioName = "";
    this.date = "";
  }

  @Override
  public void createPortfolio(String portfolioName, String date) {
    if (checkPortfolioAlreadyExists(portfolioName)) {
      if (getMapSize(portfolioName) != 0) {
        throw new IllegalArgumentException("CANNOT MODIFY A LOCKED PORTFOLIO.");
      } else {
        throw new IllegalArgumentException(
            "PORTFOLIO ALREADY PRESENT. ADD STOCKS.");
      }
    }
    this.date = date;
    this.portfolioName = portfolioName;
    this.map.put(portfolioName, new HashMap<>());
    this.map.get(portfolioName).put(date, new HashMap<>());
  }

  @Override
  public boolean checkPortfolioAlreadyExists(String name) {
    return map.containsKey(name);
  }

  @Override
  public void buyStock(String portfolioName, String ticker, int quantity, double price) {
    if (!map.containsKey(portfolioName)) {
      throw new IllegalArgumentException("Enter valid portfolio name.");
    }
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
      this.totalValue = totalValue + Math.round(quantity * price);
    } else {
      map.get(portfolioName).get(date).put(ticker, new ArrayList<String>());
      map.get(portfolioName).get(date).get(ticker).add(String.valueOf(quantity));
      map.get(portfolioName).get(date).get(ticker).add(String.valueOf(price));
      map.get(portfolioName).get(date).get(ticker).add(String.valueOf(quantity * price));
      this.totalValue = totalValue + (quantity * price);
    }
  }

  @Override
  public double costBasisByDate(String portFolioName, String date) {
    return totalValue;
  }

  @Override
  public double portfolioValue(String portfolioName, String date) {
    return 0;
  }

  @Override
  public HashMap<String, HashMap<String, HashMap<String, List<String>>>> returnMap() {
    return map;
  }


  private int getMapSize(String portfolioName) {
    return this.map.get(portfolioName).size();
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
      dates.add(dateIterator.next()); // check for last element
    }
    Collections.sort(dates);
    if (dates.get(0).equals(currentDate)) {
      return currentDate;
    }
    for (int i = 1; i < map.get(name).keySet().size(); i++) {
      if (dates.get(i).equals(currentDate)) {
        return dates.get(i - 1);
      }
    }
    return "";
  }
}

