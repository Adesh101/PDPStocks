package model.portfolio;

import java.util.HashMap;
import java.util.List;

/**
 * A public interface that contains methods to perform operations related to Inflexible portfolio.
 */
public interface IInflexiblePortfolio {

  /**
   * A method to create Inflexible Portfolio.
   * @param portfolioName portfolio name
   * @param date creation date
   */
  void createPortfolio(String portfolioName, String date);

  /**
   * A method to check if portfolio already exists.
   * @param name name of the portfolio
   * @return true if portfolio is present else false
   */
  boolean checkPortfolioAlreadyExists(String name);

  /**
   * A method to perform buy operation.
   * @param portfolioName name of the portfolio
   * @param ticker stock to be purchased
   * @param quantity quantity of stocks to be added
   * @param price price of the stock
   */
  void buyStock(String portfolioName, String ticker, int quantity, double price);

  /**
   * A method to get cost basis.
   * @param portFolioName name of the portfolio
   * @param date date to be queried
   * @return cost basis for the given date
   */
  double costBasisByDate(String portFolioName, String date);

  /**
   * A method to calculate portfolio value for a given date.
   * @param portfolioName portfolio name
   * @param date date
   * @return portfolio value for the given date
   */
  double portfolioValue(String portfolioName, String date);

  /**
   * A method to return map in which all the inflexible portfolios are stored.
   * @return map
   */
  HashMap<String, HashMap<String, HashMap<String, List<String>>>> returnMap();

  /**
   * A method to calculate the previous date present in the map.
   * @param currentDate current date
   * @param name name of portfolio
   * @return previous dae present in the map
   */
  String getPreviousDate(String currentDate, String name);
}
