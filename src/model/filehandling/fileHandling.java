package model.filehandling;

public interface fileHandling {
  boolean checkLocalData(String ticker);
  String[] readFromLocalData(String ticker, String date);
  void addToLocalData(String ticker,StringBuilder data);
  boolean isTickerValid(String ticker);
  void updateFile(String file, String data);
  String getMostRecentDate(String file);
  boolean checkIfRecentDateIsCurrentDate(String date);
}
