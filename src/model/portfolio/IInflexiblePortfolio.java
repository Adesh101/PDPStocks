package model.portfolio;

import java.util.HashMap;
import java.util.List;

public interface IInflexiblePortfolio {
  void createPortfolio(String portfolioName, String date);
  boolean checkPortfolioAlreadyExists(String name);
  void buyStock(String portfolioName, String ticker, int quantity, double price);
  //void sellStock(String portfolioName, String ticker, int quantity, double price);
  double costBasisByDate(String portFolioName, String date);
  double portfolioValue(String portfolioName, String date);
  HashMap<String, HashMap<String, HashMap<String, List<String>>>> returnMap();
  String getPreviousDate(String currentDate, String name);
}
