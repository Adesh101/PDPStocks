package model.portfolio;

import java.util.HashMap;
import java.util.List;

public interface IFlexiblePortfolio {
  void createPortfolio(String portfolioName, String date);
  boolean checkPortfolioAlreadyExists(String name);
  void buyStock(String portfolioName, String ticker, int quantity, double price, String date);
  void sellStock(String portfolioName, String ticker, int quantity, double price, String date);
  double costBasisByDate(String portFolioName, String date);
  double portfolioValue(String portfolioName, String date);
  HashMap<String, HashMap<String, HashMap<String, List<String>>>> returnMap();
  HashMap<String, HashMap<String, Double>> returnCostBasisMap();
}
