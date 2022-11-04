package model.operation;

import java.util.HashMap;
import java.util.List;

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
   * A method that gives the current price of the stock.
   * @return:  the price in double
   * @param: ticker symbol of the stock whose price needs to be fetched
   */
  double getCurrentPrice(String ticker);

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
   * A method to fetch a particular portfolio value on a given date.
   * @param: map
   * @param: date
   * @return: value of portfolio
   */
  double callStockAPIByDateHelper(HashMap<String, List<String>> map, String date);

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
}
