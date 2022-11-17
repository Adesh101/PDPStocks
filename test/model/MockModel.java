package model;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
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
  public String getPortfolioComposition(String portfolioName) {
    log.append("PORTFOLIO COMPOSITION: \n");
    return null;
  }

  @Override
  public double getPortfolioByDate(String portfolioName, String date) {
    log.append("PORTFOLIO NAME: " + portfolioName + " " + " DATE: " + date + "\n");
    return 0;
  }

  @Override
  public String[] callStockAPI(String ticker, String date) {
    log.append("STOCK NAME: " + ticker + " DATE: " + date + "\n");
    return new String[0];
  }

  @Override
  public void createFlexiblePortfolio(String portfolioName, String date) {
    log.append("PORTFOLIO NAME: " + portfolioName + " " + " DATE: " + date + "\n");
  }

  @Override
  public void createLockedPortfolio(String portfolioName) {
    log.append("PORTFOLIO NAME: " + portfolioName + "\n");
  }

  @Override
  public Date yesterdaysDate() {

    return null;
  }

  @Override
  public boolean checkWhetherFlexible(String portFolioName) {
    log.append("PORTFOLIO NAME: " + portFolioName + "\n");
    return true;
  }

  @Override
  public boolean checkWhetherInflexible(String portFolioName) {
    log.append("PORTFOLIO NAME: " + portFolioName + "\n");
    return true;
  }

  @Override
  public void getFlexibleMap() {
    log.append("Flexible map");
  }

  @Override
  public void getInflexibleMap() {
    log.append("Inflexible map.");
  }

  @Override
  public void addStockToFlexiblePortfolio(String portfolioName, String ticker, int quantity,
      double price, String date, double fee) {
    log.append("PORTFOLIO NAME: " + portfolioName + " STOCK: " + ticker + " QUANTITY: " + quantity
        + " PRICE: " + price + " DATE: " + date + " FEE: " + fee + "\n");
  }

  @Override
  public void addStockToInFlexiblePortfolio(String portfolioName, String ticker, int quantity,
      double price) {
    log.append("PORTFOLIO NAME: " + portfolioName + " STOCK: " + ticker + " QUANTITY: " + quantity
        + " PRICE: " + price + "\n");
  }

  @Override
  public double costBasisByDate(String portfolioName, String date) {
    return 0;
  }

  @Override
  public int getFlexibleMapSize() {
    return 0;
  }

  @Override
  public void sellStock(String portfolioName, String ticker, int quantity, double price,
      String date, double fee) {
    log.append("Sell stock.");
  }

  @Override
  public HashMap<String, Integer> returnPortfolioData(String portfolioName) {
    return null;
  }

  @Override
  public String getPreviousDate(HashMap<String, HashMap<String, HashMap<String, List<String>>>> map,
      String currentDate, String name) {
    return null;
  }

  @Override
  public TreeMap<String, Integer> getGraph(String portfolioName, String startDate, String endDate) {
    return null;
  }

  @Override
  public int getLineChartScale() {
    return 0;
  }
}
