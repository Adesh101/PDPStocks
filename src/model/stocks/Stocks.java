package model.stocks;

/**
 * This class is used to handle stock data that we fetch from an API.
 */
public class Stocks implements IStocks {

  String[] stockData;
  FetchStockData data;

  public Stocks() {
    this.stockData = new String[50];
    this.data = new FetchStockData();
  }

  @Override
  public String[] callStockAPI(String ticker) {
    this.stockData = data.callStockAPI(ticker);
    return this.stockData;
  }


  @Override
  public void callStockAPIByDate(String ticker, String date) {
    this.stockData = data.callStockAPIbyDate(ticker, date);
  }

  @Override
  public double getStockCurrentPrice(String ticker) {
    return Double.parseDouble(this.stockData[13]);
  }

  @Override
  public double getStockCurrentPriceByDate(String ticker) {
    if (this.stockData.length > 2) {
      return Double.parseDouble(this.stockData[1]);
    }
    return 0;
  }
}
