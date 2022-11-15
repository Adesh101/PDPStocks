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
  void createPortfolioHelper();
  void createFlexiblePortfolio();
  void createInflexiblePortfolio();
  void showExistingPortfolioHelper();
  void showAmountByDateHelper();
  void showCompositionHelper();
  void showCostBasisByDateHelper();
  void sellStockHelper();
  void createPortfolioCSV();
  void addStocksHelper(String portfolioName);
  void addStocksToFlexiblePortfolioHelper();
  void showGraph();
  String menuHelper();
}
