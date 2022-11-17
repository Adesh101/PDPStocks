package model.filehandling;

import java.util.HashMap;
import java.util.List;

/**
 * Class to handle files.
 */
public interface FileHandling {

  /**
   * Method to check local data.
   * @param: ticker
   * @return: true/false value
   */
  boolean checkLocalData(String ticker);

  /**
   * Method to read from local data.
   * @param: ticker
   * @param: date
   * @return: string array
   */
  String[] readFromLocalData(String ticker, String date);

  /**
   * Method to add to local data.
   * @param: ticker
   * @param: data
   */
  void addToLocalData(String ticker, StringBuilder data);

  /**
   * Method to check ticker validity.
   * @param: ticker
   * @return: true/false value
   */
  boolean isTickerValid(String ticker);

  /**
   * Method to update local files.
   * @param: file
   * @param: data
   */
  void updateFile(String file, String data);

  /**
   * Method to get the most recent date.
   * @param: file
   * @return: most recent date
   */
  String getMostRecentDate(String file);

  /**
   * Method to check if the recent date is current date.
   * @param: date
   * @return: true/false value
   */
  boolean checkIfRecentDateIsCurrentDate(String date);

  /**
   * Method to check map type.
   * @param: fileName
   * @return: int value with map type
   */
  int checkMapType(String fileName);

  /**
   * Method to write data to CSV files.
   * @param: portfolioName
   * @param: map
   * @param: portfolioType
   */
  void writeToCSV(String portfolioName,
      HashMap<String, HashMap<String, HashMap<String, List<String>>>> map, String portfolioType);

  /**
   * Method to read from file the hashmap data.
   * @param: portfolioName
   * @return: hashmap with their respective values
   */
  HashMap<String, HashMap<String, HashMap<String, List<String>>>> readFromFile(
      String portfolioName);
}
