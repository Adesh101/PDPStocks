package model.portfolio;

import java.util.HashMap;
import java.util.List;

/**
 * A public interface that contains methods to perform operations related to Flexible Portfolio.
 */
public interface IFlexiblePortfolio {

  /**
   * A method to create flexible portfolio.
   * @param portfolioName name of the portfolio to be created
   * @param date creation date
   */
  void createPortfolio(String portfolioName, String date);

  /**
   * A method to check if portfolio already exists.
   * @param name name of the portfolio
   * @return returns true if portfolio is already present else false
   */
  boolean checkPortfolioAlreadyExists(String name);

  /**
   * A method to perform buy operation.
   * @param portfolioName name of the portfolio
   * @param ticker stock to be bought
   * @param quantity quantity of the stock to be purchased
   * @param price price of the stock
   * @param date date of purchase
   * @param fee commission fee for the transaction
   */
  void buyStock(String portfolioName, String ticker, int quantity,
      double price, String date, double fee);

  /**
   * A method to perform sell operation.
   * @param portfolioName name of the portfolio
   * @param ticker stock to be sold
   * @param quantity quantity to be sold
   * @param price price of the stock
   * @param date date of selling
   * @param fee commission fee for the transaction
   */
  void sellStock(String portfolioName, String ticker, int quantity,
      double price, String date, double fee);

  /**
   * A method to calculate cost basis of a portfolio for a given date.
   * @param portFolioName name of the portfolio
   * @param date given date
   * @return cost basis
   */
  double costBasisByDate(String portFolioName, String date);

  /**
   * A method to calculate portfolio value.
   * @param portfolioName portfolio name
   * @param date date
   * @return portfolio value
   */
  double portfolioValue(String portfolioName, String date);

  /**
   * A method to return the map in which flexible portfolios are stored.
   * @return map
   */
  HashMap<String, HashMap<String, HashMap<String, List<String>>>> returnMap();

  /**
   * A method to return the map in which cost basis data is stored.
   * @return map
   */
  HashMap<String, HashMap<String, Double>> returnCostBasisMap();

  /**
   * A method to get previous date entry in the map.
   * @param currentDate current date
   * @param name name of portfolio
   * @return returns the previous date present in hashmap
   */
  String getPreviousDate(String currentDate, String name);
}
