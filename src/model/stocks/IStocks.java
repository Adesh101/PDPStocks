package model.stocks;

/**
 * Interface for the Stock class.
 */
public interface IStocks {

  /**
   * This method will fetch the stock data for a particular ticker.
   * @param: ticker
   * @return: string array with stock data
   */
  String[] callStockAPI(String ticker);

  /**
   * This method will fetch the stock data for a particular ticker and a particular date.
   * @param: ticker
   * @param: date
   *
   */
  void callStockAPIByDate(String ticker, String date);

  /**
   * This method will fetch the current price for a particular ticker.
   * @param: ticker
   * @return: price of the stock
   */
  double getStockCurrentPrice(String ticker);

  /**
   * This method will fetch the current price for a particular ticker at a current date.
   * @param: ticker
   * @return: value of a stock at the given date
   */
  double getStockCurrentPriceByDate(String ticker);
}
