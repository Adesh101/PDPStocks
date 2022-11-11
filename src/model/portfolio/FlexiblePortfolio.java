package model.portfolio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FlexiblePortfolio implements IPortfolio {
  protected HashMap<String, HashMap<String, List<String>>> portfolios
      = new HashMap<String, HashMap<String, List<String>>>();
  protected String portfolioName;
  protected double totalValue;

  public FlexiblePortfolio(){
    this.portfolioName="";
    this.totalValue=0;
  }

  @Override
  public void createPortfolio(String portfolioName) {
    if (checkPortfolioAlreadyExists(portfolioName)) {
        throw new IllegalArgumentException(
            "PORTFOLIO ALREADY PRESENT. ADD STOCKS.");
    }
    this.portfolios.put(portfolioName, new HashMap<String, List<String>>());
    this.portfolioName = portfolioName;
  }

  @Override
  public boolean checkPortfolioAlreadyExists(String name) {
    return portfolios.containsKey(name);
  }

  @Override
  public void buyStock(String portfolioName, String ticker, int quantity, double price) {
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
  public void sellStock(String portfolioName, String ticker, int quantity, double price) {
    if(!portfolios.containsKey(portfolioName)){
      throw new IllegalArgumentException("Enter valid portfolio name.");
    }
    if (portfolios.get(portfolioName).containsKey(ticker)) {
      int existingNoOfStocks = Integer.parseInt(portfolios.get(portfolioName).get(ticker).get(0));
      portfolios.get(portfolioName).get(ticker)
          .set(0, String.valueOf(existingNoOfStocks - quantity));
      double existingPrice = Double.parseDouble(portfolios.get(portfolioName).get(ticker).get(1));
      portfolios.get(portfolioName).get(ticker).set(1, String.valueOf((existingPrice + price) / 2)); // chrck
      double existingTotalStockValue = Double.parseDouble(
          portfolios.get(portfolioName).get(ticker).get(2));
      portfolios.get(portfolioName).get(ticker)
          .set(2, String.valueOf(existingTotalStockValue - (quantity * price)));
      this.totalValue = totalValue - Math.round(quantity * price);
    } else {
       throw new IllegalArgumentException("No such stock present.");
    }
  }

  @Override
  public double costBasisByDate(String portFolioName, String date) {
    return totalValue;
  }


}
