package controller;

import model.operation.IOperation;

/**
 * This is an interface for the Controller.
 */
public interface IController {

  /**
   * This is a method starts the program.
   */
  void operate(IOperation operation);

  /**
   * Helper for create portfolio.
   */
  void createPortfolioHelper();

  /**
   * Helper for create flexible portfolio.
   */
  void createFlexiblePortfolio();

  /**
   * Helper to create inflexible portfolio.
   */
  void createInflexiblePortfolio();

  /**
   * Helper to show all existing portfolios.
   */
  void showExistingPortfolioHelper();

  /**
   * Helper to show portfolio amount by date.
   */
  void showAmountByDateHelper();

  /**
   * Helper to show portfolio composition.
   */
  void showCompositionHelper();

  /**
   * Helper to show cost basis of a portfolio.
   */
  void showCostBasisByDateHelper();

  /**
   * Helper to sell stocks.
   */
  void sellStockHelper();

  /**
   * Helper to create portfolio through CSV.
   */
  void createPortfolioCSV();

  /**
   * Helper to add stocks.
   *
   * @param: portfolioName
   */
  void addStocksHelper(String portfolioName);

  /**
   * Helper to add stocks to a flexible portfolio.
   */
  void addStocksToFlexiblePortfolioHelper();

  /**
   * Helper to show graph.
   */
  void showGraph();

  /**
   * Helper to show menu.
   *
   * @return: menuOption
   */
  String menuHelper();
}
