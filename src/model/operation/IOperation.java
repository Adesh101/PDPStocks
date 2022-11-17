package model.operation;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

/**
 * This is an interface for the operations to be performed.
 */
public interface IOperation {

  /**
   * A method to add a new portfolio name to the hashmap.
   * @param: portfolioName name of the new portfolio
   */
  void createNewPortfolio(String portfolioName);

  /**
   * A method to add  a stock to the existing portfolio.
   * @param: portfolioName name of the portfolio in which stock is to be added
   * @param: ticker symbol of the stock to be added
   * @param: quantity of the stock to be added
   * @param: price of the stock to be added
   */
  void addStockToPortfolio(String portfolioName, String ticker, int quantity, double price);


  /**
   * A method that gives all the existing portfolios.
   * @return: portfolios
   */
  String getExistingPortfolios();

  /**
   * A method that gives the number of stocks stored in a portfolio.
   * @return:  the count as integer
   * @param: portfolioName name of the portfolio
   */
  int getMapSize(String portfolioName);


  /**
   * A method to check whether a portfolio already exists.
   * @return:  true if the portfolio already exists else returns false
   * @param: name of the portfolio to be checked
   */
  boolean checkPortfolioAlreadyExists(String name);

  /**
   * A method to check whether the ticker is valid.
   * @return:  true if ticker is valid else returns false
   * @param: ticker stock of the symbol
   */
  boolean isTickerValid(String ticker);

  /**
   * A method to write the portfolio data to a csv file.
   * @param: portfolios (hashmap)
   */
  void writeToCSV(String portfolioName);

  /**
   * A method to read input from a particular file.
   * @param: fileName
   * @return: parsed string input
   */
  String readFromFile(String fileName);

  /**
   * Returns a list of portfolio names.
   * @return: arraylist of portfolios
   */
  List<String> getStockNamesFromPortfolio();

  /**
   * Fetches data of a particular portfolio.
   * @return: portfolio data
   */
  HashMap<String, HashMap<String, List<String>>> getPortfolioMap();


  /**
   * Fetches the data of a particular portfolio.
   * @param: portfolioName
   * @return: string parsed data
   */
  String getPortfolioComposition(String portfolioName);

  /**
   * Fetches portfolio data on a particular date.
   * @param: portfolioName
   * @param: date
   * @return: total value of portfolio
   */
  double getPortfolioByDate(String portfolioName, String date);

  /**
   * Method to fetch API data.
   * @param: ticker
   * @param: date
   * @return: string array
   */
  String[] callStockAPI(String ticker, String date);

  /**
   * Method to create flexible portfolio.
   * @param: portfolioName
   * @param: date
   */
  void createFlexiblePortfolio(String portfolioName, String date);

  /**
   * Method to create locked portfolio.
   * @param: portfolioName
   */
  void createLockedPortfolio(String portfolioName);

  /**
   * Method to check yesterday's date.
   * @return: date
   */
  Date yesterdaysDate();

  /**
   * Method to check flexible portfolio.
   * @param: portFolioName
   * @return: true/false value
   */
  boolean checkWhetherFlexible(String portFolioName);

  /**
   * Method to check inflexible portfolio.
   * @param: portFolioName
   * @return: inflexible portfolio
   */
  boolean checkWhetherInflexible(String portFolioName);

  /**
   * Method to check flexible map.
   */
  void getFlexibleMap();

  /**
   * Method to check inflexible map.
   */
  void getInflexibleMap();

  /**
   * Method to add stock.
   * @param: portfolioName
   * @param: ticker
   * @param: quantity
   * @param: price
   * @param: date
   * @param: fee
   */
  void addStockToFlexiblePortfolio(String portfolioName, String ticker, int quantity,
      double price, String date,double fee);

  /**
   * Method to add stock.
   * @param: portfolioName
   * @param: ticker
   * @param: quantity
   * @param: price
   */
  void addStockToInFlexiblePortfolio(String portfolioName, String ticker, int quantity,
      double price);

  /**
   * Method to check cost basis.
   * @param: portfolioName
   * @param: date
   * @return: cost basis map
   */
  double costBasisByDate(String portfolioName, String date);

  /**
   * Method to get flexible map size.
   * @return: map size
   */
  int getFlexibleMapSize();

  /**
   * Method to sell stock.
   * @param: portfolioName
   * @param: ticker
   * @param: quantity
   * @param: price
   * @param: date
   * @param: fee
   */
  void sellStock(String portfolioName, String ticker, int quantity,
      double price, String date, double fee);

  /**
   * Method to return portfolio data.
   * @param: portfolioName
   * @return: portfolio data
   */
  HashMap<String, Integer> returnPortfolioData(String portfolioName);

  /**
   * Method to fetch previous date.
   * @param: map
   * @param: currentDate
   * @param: name
   * @return: get previous date
   */
  public String getPreviousDate(HashMap<String, HashMap<String, HashMap<String, List<String>>>> map,
      String currentDate, String name);

  /**
   * Method to store graph data.
   * @param: portfolioName
   * @param: startDate
   * @param: endDate
   * @return: graph data
   */
  TreeMap<String, Integer> getGraph(String portfolioName, String startDate, String endDate);

  /**
   * Method to fetch line chart.
   * @return: line chart
   */
  int getLineChartScale();
}
