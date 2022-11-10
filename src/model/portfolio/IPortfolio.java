package model.portfolio;

public interface IPortfolio {
  void createPortfolio(String portfolioName);
  boolean checkPortfolioAlreadyExists(String name);
  void buyStock(String portfolioName, String ticker, int quantity, double price);
  void sellStock(String portfolioName, String ticker, int quantity, double price);
  double costBasisByDate(String portFolioName, String date);

}
