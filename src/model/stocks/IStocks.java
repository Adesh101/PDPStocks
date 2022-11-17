package model.stocks;

/**
 * Interface for the Stock class.
 */
public interface IStocks {

  /**
   * This method will fetch the stock data for a particular ticker.
   *
   * @param: ticker
   * @return: string array with stock data
   */
  String[] callStockAPI(String ticker, String date);

  /**
   * Method to check if the day is Saturday/Sunday.
   *
   * @param: date
   * @return: string value
   */
  String isWeekend(String date);

  /**
   * Method to update files.
   *
   * @param: file
   */
  void updateFile(String file);

  /**
   * Method to get price by date.
   *
   * @param: ticker
   * @param: date
   * @return: price
   */
  double getPriceByDate(String ticker, String date);

}
