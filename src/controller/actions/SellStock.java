package controller.actions;

import model.operation.IOperation;

/**
 * Class to sell stock.
 */
public class SellStock implements IActions {

  private String portfolioName;
  private String ticker;
  private int quantity;
  private double price;
  private String date;
  double fee;

  /**
   * Constructor for sell stock.
   * @param: portfolioName
   * @param: ticker
   * @param: quantity
   * @param: date
   * @param: fee
   */
  public SellStock(String portfolioName, String ticker, int quantity, String date, double fee) {
    this.portfolioName = portfolioName;
    this.ticker = ticker;
    this.quantity = quantity;
    this.price = 0;
    this.date = date;
    this.fee = fee;
  }

  @Override
  public String operate(IOperation operation) {
    if (operation.checkWhetherFlexible(portfolioName)) {
      if (operation.getFlexibleMapSize() != 0) {
        this.price = Double.parseDouble(operation.callStockAPI(this.ticker, this.date)[3]);
        operation.sellStock(portfolioName, ticker, quantity, price, date, fee);
        return "ORDER PLACED SUCCESSFULLY";
      }
      throw new IllegalArgumentException("PORTFOLIO IS EMPTY");
    }
    throw new IllegalArgumentException("THIS PORTFOLIO IS LOCKED");
  }
}
