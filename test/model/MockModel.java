package model;

import java.util.HashMap;
import java.util.List;
import model.operation.IOperation;

/**
 * Mock Model to test controller.
 */
public class MockModel implements IOperation {

  private StringBuilder log;

  public MockModel(StringBuilder log) {
    this.log = log;
  }

  @Override
  public void createNewPortfolio(String portfolioName) {
    log.append("PORTFOLIO: " + portfolioName + " " + " CREATED SUCCESSFULLY. ");
  }

  @Override
  public void addStockToPortfolio(String portfolioName, String ticker, int quantity, double price) {
    log.append("PORTFOLIO NAME: " + portfolioName + " " + "STOCK: " + ticker + " " + "QUANTITY: "
        + quantity + " " + "PRICE: " + price + " " + "\n");
  }

  @Override
  public String getExistingPortfolios() {
    log.append("PORTFOLIO LIST:\n");
    return null;
  }

  @Override
  public int getMapSize(String portfolioName) {
    log.append("PORTFOLIO NAME: " + portfolioName + "\n");
    return 0;
  }

  @Override
  public double getCurrentPrice(String ticker) {
    log.append("TICKER: " + ticker + "\n");
    return 0;
  }

  @Override
  public boolean checkPortfolioAlreadyExists(String name) {
    log.append("PORTFOLIO NAME: " + name + "\n");
    return true;
  }

  @Override
  public boolean isTickerValid(String ticker) {
    log.append("TICKER: " + ticker + "\n");
    return true;
  }

  @Override
  public void writeToCSV(String portfolios) {
    log.append("HASHMAP: " + portfolios + "\n");
  }

  @Override
  public String readFromFile(String fileName) {
    log.append("FILE REACHED.\n");
    return null;
  }

  @Override
  public List<String> getStockNamesFromPortfolio() {
    log.append("FETCHING STOCK NAMES FROM PORTFOLIO.\n");
    return null;
  }

  @Override
  public HashMap<String, HashMap<String, List<String>>> getPortfolioMap() {
    log.append("FETCHING PORTFOLIO.\n");
    return null;
  }

  @Override
  public double callStockAPIByDateHelper(HashMap<String, List<String>> map, String date) {
    log.append("MAP: " + map + " " + "DATE: " + date + "\n");
    return 0;
  }

  @Override
  public String getPortfolioComposition(String portfolioName) {
    log.append("PORTFOLIO COMPOSITION: \n");
    return null;
  }

  @Override
  public double getPortfolioByDate(String portfolioName, String date) {
    log.append("PORTFOLIO NAME: " + portfolioName + " " + " DATE: " + date + "\n");
    return 0;
  }
}
