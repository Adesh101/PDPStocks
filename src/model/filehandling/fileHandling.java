package model.filehandling;

import java.util.HashMap;
import java.util.List;

public interface fileHandling {
  boolean checkLocalData(String ticker);
  String[] readFromLocalData(String ticker, String date);
  void addToLocalData(String ticker,StringBuilder data);
  boolean isTickerValid(String ticker);
  void updateFile(String file, String data);
  String getMostRecentDate(String file);
  boolean checkIfRecentDateIsCurrentDate(String date);
  int checkMapType(String fileName);

  void writeToCSV(String portfolioName, HashMap<String, HashMap<String, HashMap<String, List<String>>>> map, String portfolioType);
  HashMap<String, HashMap<String, HashMap<String, List<String>>>> readFromFile(String portfolioName);
}
